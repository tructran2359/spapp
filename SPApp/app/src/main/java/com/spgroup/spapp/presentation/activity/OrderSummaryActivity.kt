package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.*
import com.spgroup.spapp.presentation.adapter.PreferredTimeAdapter
import com.spgroup.spapp.presentation.view.*
import com.spgroup.spapp.presentation.viewmodel.CustomiseViewModel
import com.spgroup.spapp.presentation.viewmodel.OrderSummaryViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.isNumberOnly
import com.spgroup.spapp.util.extension.isValidEmail
import kotlinx.android.synthetic.main.activity_order_summary.*

class OrderSummaryActivity : BaseActivity() {

    companion object {

        @JvmField val RC_EDIT = 11

        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, OrderSummaryActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation
    private var mFirstInvalidView: ValidationInputView? = null
    private lateinit var mViewModel: OrderSummaryViewModel

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        initAnimations()
        initViews()

        subscribeUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_EDIT && resultCode == RESULT_OK) {
            data?.let {
                val content = data.getSerializableExtra(ConstUtils.EXTRA_CONTENT) as CustomiseViewModel.Content
                val serviceId = data.getIntExtra(ConstUtils.EXTRA_SERVICE_ID, -1)
                if (serviceId != -1) {
                    val service = mViewModel.getServiceById(serviceId)
                    if (service != null) {
                        val view = ll_item_container.findViewWithTag<SummaryItemViewCombo>(createServiceTag(serviceId))
                        (service as ServiceItemComboDummy).run {
                            this.dummyContent = content
                            view.setDummyData(this.dummyContent)
                        }
                    }
                }
            }
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun subscribeUI() {
        val factory = ViewModelFactory.getInstance()
        mViewModel = ViewModelProviders.of(this, factory).get(OrderSummaryViewModel::class.java)

        mViewModel.run {

            mListNormalizedData.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    doLogD("Dummy", it.toString())
                    addView(it)
                }
            })

            mDeletedServiceId.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    val view = ll_item_container.findViewWithTag<View>(createServiceTag(it))
                    ll_item_container.removeView(view)
                }
            })

            mDeletedCateId.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    val view = ll_item_container.findViewWithTag<View>(createCateTag(it))
                    ll_item_container.removeView(view)
                }
            })

            initData(createDummyData())
        }
    }

    private fun initAnimations() {
        mAnimAppear = AnimationUtils.loadAnimation(this, R.anim.anim_slide_up)
        mAnimDisappear = AnimationUtils.loadAnimation(this, R.anim.anim_slide_down)

        mAnimAppear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                rl_error_cointainer.visibility = View.VISIBLE
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })

        mAnimDisappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                rl_error_cointainer.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    private fun initViews() {

        action_bar.setTitle(R.string.summary)

        validation_name.setValidation { name: String -> name.length > 1 }

        validation_email.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        validation_email.setValidation { emailAddress: String -> emailAddress.isValidEmail() }

        validation_contact_no.setInputType(InputType.TYPE_CLASS_NUMBER)
        validation_contact_no.setValidation { phone: String -> phone.length == 8 && phone.isNumberOnly() }

        validation_postal_code.setInputType(InputType.TYPE_CLASS_NUMBER)
        validation_postal_code.setValidation { postalCode: String -> postalCode.isNumberOnly() }

        with(btn_summary) {
            isEnabled = true
            setText(getString(R.string.submit_request))
            setCount(1)
            setEstPrice(0.01f)
            setOnClickListener {
                var invalidCount = 0
                mFirstInvalidView = null

                val listValidationField = listOf(
                        validation_postal_code,
                        validation_address,
                        validation_contact_no,
                        validation_email,
                        validation_name)

                listValidationField.forEach {
                    val valid = it.validate()
                    if (!valid) {
                        invalidCount++
                        mFirstInvalidView = it
                    }
                }
                if (invalidCount == 0) {
                    rl_error_cointainer.visibility = View.GONE
                    startActivity(AcknowledgementActivity.getLaunchIntent(this@OrderSummaryActivity))
                } else {
                    var errorMsg = if(invalidCount == 1) {
                        this@OrderSummaryActivity.getString(R.string.error_detected_1)
                    } else {
                        this@OrderSummaryActivity.getString(R.string.error_detected_format, invalidCount)
                    }
                    tv_error_counter.setText(errorMsg)
                    showErrorView(true)
                }
            }
        }

        action_bar.setOnBackPress {
            onBackPressed()
        }

        val list = listOf("Select Time", "11AM - 12PM", "12PM - 2PM", "2PM - 4PM")
        val adapter = PreferredTimeAdapter(this, R.layout.layout_preferred_time, list)

        spinner_preferred_time.adapter = adapter

        rl_error_cointainer.setOnClickListener {
            mFirstInvalidView?.let {
                scroll_content.scrollTo(0, 0)
                val location = IntArray(2)
                it.getLocationOnScreen(location)
                doLogD("Scroll", "Pos on screen: ${location[0]} , ${location[1]}")
                val position = location[1] - getDimensionPixelSize(R.dimen.action_bar_height) - getDimensionPixelSize(R.dimen.common_vert_large)
                scroll_content.scrollTo(0, position)
                it.requestFocus()
                showErrorView(false)
            }
        }
    }

    private fun addView(list: List<NormalizedSummaryData>?) {
        ll_item_container.removeAllViews()
        if (list == null || list.isEmpty()) return

        val inflater = LayoutInflater.from(this)

        for (data in list) {
                addHeader(inflater, data.cateName, data.cateId, data.needHeader())

            for (item in data.listService) {
                when (item) {
                    is ServiceItemCounter -> addItemCounter(item)

                    is ServiceItemCheckBox -> addItemCheckbox(item)

                    is ServiceItemComboDummy -> addItemCombo(item)
                }
            }
        }
    }

    private fun addHeader(inflater: LayoutInflater, cateName: String, cateId: Int, needHeader: Boolean) {
        val tag = createCateTag(cateId)
        if (!needHeader) {
            val view = View(this)
            view.setTag(tag)
            view.visibility = View.GONE
            ll_item_container.addView(view)
            ll_item_container.setPadding(0, getDimensionPixelSize(R.dimen.common_horz_large), 0, 0)
            return
        }

        val tvHeader = inflater.inflate(R.layout.item_summary_header, null, false) as TextView
        val layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_large)
            leftMargin = getDimensionPixelSize(R.dimen.common_horz_large)
            rightMargin = getDimensionPixelSize(R.dimen.common_horz_large)
        }

        tvHeader.run {
            setText(cateName)
            setLayoutParams(layoutParams)
            setTag(tag)
        }
        ll_item_container.addView(tvHeader)
    }

    private fun addItemCounter(item: ServiceItemCounter) {
        val tag = createServiceTag(item.id)
        val view = SummaryItemView(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setName(item.name)
            initData(item.minCount, item.maxCount, item.count)
            setTag(tag)
            setOnCountChangedListener(object : CounterView.OnCountChangeListener {
                override fun onPlus() {
                    item.count++
                    setCount(item.count)
                }

                override fun onMinus() {
                    item.count--
                    setCount(item.count)
                }
            })
            setOnDeleteListener {
                mViewModel.deleteService(item.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun addItemCheckbox(item: ServiceItemCheckBox) {
        val tag = createServiceTag(item.id)
        val view = SummaryItemViewEstimated(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setName(item.name)
            setDescription(item.description)
            setTag(tag)
            setOnDeleteListener {
                mViewModel.deleteService(item.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun addItemCombo(item: ServiceItemComboDummy) {
        val tag = createServiceTag(item.id)
        val view = SummaryItemViewCombo(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setDummyData(item.dummyContent)
            setOnEditClickListener {
                val dummy = item.dummyContent
                val intent = CustomiseActivity.getLaunchIntent(
                        this@OrderSummaryActivity,
                        item.toServiceItemCombo(),
                        dummy,
                        true)
                startActivityForResult(intent, RC_EDIT)
            }
            setTag(tag)
            setOnDeleteListener {
                mViewModel.deleteService(item.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun showErrorView(show: Boolean) {
        if (show && rl_error_cointainer.visibility != View.VISIBLE) {
            rl_error_cointainer.startAnimation(mAnimAppear)
        } else if (!show && rl_error_cointainer.visibility == View.VISIBLE) {
            rl_error_cointainer.startAnimation(mAnimDisappear)
        }
    }

    private fun createDummyData(): List<ServiceCategory> {

        val serviceCounter1 = ServiceItemCounter(id = 1, name = "Counter 1", price = 1f, minCount = 1, maxCount = 10, unit = "item", count = 1)
        val serviceCounter2 = ServiceItemCounter(id = 2, name = "Counter 2", price = 2f, minCount = 1, maxCount = 10, unit = "item", count = 2)
        val serviceCheckbox3 = ServiceItemCheckBox(id = 3, name = "Checkbox 3", price = 3f, unit = "item", description = "Description 3", selected = true)

        val dummy = CustomiseViewModel.Content(paxCount = 1, riceCount = 1, instruction = "No beef and peanut. Low salt.")
        val serviceItemCombo4 = ServiceItemComboDummy(id = 4, name = "Customise 4", price = 4f, unit = "item", description = "Description 4", selected = true, dummyContent = dummy)

        val subCate1 = ServiceGroup(id = 1, name = "SubCate 1", description = "Sub description 1", listItem = mutableListOf(serviceItemCombo4), expanded = true)
        val subCate2 = ServiceGroup(id = 2, name = "SubCate 2", description = "Sub description 2", listItem = mutableListOf(serviceCounter1, serviceCounter2, serviceCheckbox3), expanded = true)

        val cate1 = ServiceCategory(id = 1, title = "Category 1", services = listOf(subCate1))
        val cate2 = ServiceCategory(id = 2, title = "Category 2", services = listOf(subCate2))

        return listOf(cate1, cate2)

    }

    private fun createCateTag(cateId: Int) = "Cate" + cateId

    private fun createServiceTag(serviceId: Int) = "ServiceID${serviceId}"
}
