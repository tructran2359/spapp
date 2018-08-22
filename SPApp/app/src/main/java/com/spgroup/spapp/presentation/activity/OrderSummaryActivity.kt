package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
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
import com.spgroup.spapp.presentation.viewmodel.*
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_order_summary.*

class OrderSummaryActivity : BaseActivity() {

    companion object {

        const val RC_EDIT = 11
        const val EXTRA_CATE_INFO_MAP = "EXTRA_CATE_INFO_MAP"
        const val EXTRA_SERVICE_MAP = "EXTRA_SERVICE_MAP"

        fun getLaunchIntent(
                context: Context,
                mapCateInfo: HashMap<String, String>,
                mapSelectedServices: HashMap<String, MutableList<ISelectedService>>): Intent {
            val intent = Intent(context, OrderSummaryActivity::class.java)
            intent.putExtra(EXTRA_CATE_INFO_MAP, mapCateInfo)
            intent.putExtra(EXTRA_SERVICE_MAP, mapSelectedServices)
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

        mViewModel = obtainViewModel(OrderSummaryViewModel::class.java, ViewModelFactory.getInstance())
        subscribeUI()
        val mapCateInfo = intent.getSerializableExtra(EXTRA_CATE_INFO_MAP) as HashMap<String, String>
        val mapSelectedServices = intent.getSerializableExtra(EXTRA_SERVICE_MAP) as HashMap<String, MutableList<ISelectedService>>
        mViewModel.initData(mapCateInfo, mapSelectedServices)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_EDIT && resultCode == RESULT_OK) {
//            data?.let {
//                val content = data.getSerializableExtra(ConstUtils.EXTRA_CONTENT) as CustomiseViewModel.Content
//                val serviceId = data.getIntExtra(ConstUtils.EXTRA_SERVICE_ID, -1)
//                if (serviceId != -1) {
//                    val service = mViewModel.getServiceById(serviceId)
//                    if (service != null) {
//                        val view = ll_item_container.findViewWithTag<SummaryItemViewCombo>(createServiceTag(serviceId))
//                        (service as ServiceItemComboDummy).run {
//                            this.dummyContent = content
//                            view.setDummyData(this.dummyContent)
//                        }
//                    }
//                }
//            }
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun subscribeUI() {

        mViewModel.run {

            mMapSelectedServices.observe(this@OrderSummaryActivity, Observer {
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

            mEmpty.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    if (it) {
                        startActivity(EmptyRequestActivity.getLaunchIntent(this@OrderSummaryActivity))
                        this@OrderSummaryActivity.finish()
                    }
                }
            })
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

        val list = mutableListOf("Select Time", "11AM - 12PM", "12PM - 2PM", "2PM - 4PM")
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

        setUpKeyboardDetection()
    }

    private fun setUpKeyboardDetection() {
        view_root.viewTreeObserver.addOnGlobalLayoutListener {
            val diff = view_root.rootView.height - view_root.height
            val keyboardOpen = diff > getDimensionPixelSize(R.dimen.soft_keyboar_detection_height)
            updateBottomButtonVisibility(!keyboardOpen)
        }
    }

    private fun updateBottomButtonVisibility(show: Boolean) {
        rl_summary_button_container.updateVisibility(show)
        v_shadow.updateVisibility(show)
    }

    private fun addView(mapSelectedServices: HashMap<String, MutableList<ISelectedService>>) {
        ll_item_container.removeAllViews()

        val inflater = LayoutInflater.from(this)

        for ((cateId, listSelectedServce) in mapSelectedServices) {
                addHeader(inflater, mViewModel.getCateName(cateId) ?: "", cateId )

            for (selectedService in listSelectedServce) {
                when (selectedService) {

                    is SelectedService -> {
                        if (selectedService.service is MultiplierService) {
                            addItemCounter(selectedService)
                        } else if (selectedService.service is CheckboxService) {
                            addItemCheckbox(selectedService)
                        }
                    }

                    is ComplexSelectedService -> addItemCombo(selectedService)
                }
            }
        }
    }

    private fun addHeader(inflater: LayoutInflater, cateName: String, cateId: String) {
        val tag = createCateTag(cateId)

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

    private fun addItemCounter(item: SelectedService) {
        val service = item.service as MultiplierService
        val tag = createServiceTag(service.id)
        val view = SummaryItemView(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setName(service.label)
            initData(service.min, service.max, item.count)
            setTag(tag)
            setOnCountChangedListener(object : CounterView.OnCountChangeListener {
                override fun onPlus() {
//                    item.count++
//                    setCount(item.count)
                }

                override fun onMinus() {
//                    item.count--
//                    setCount(item.count)
                }
            })
            setOnDeleteListener {
                mViewModel.deleteService(service.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun addItemCheckbox(item: SelectedService) {
        val service = item.service as CheckboxService
        val tag = createServiceTag(service.id)
        val view = SummaryItemViewEstimated(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setName(service.label)
            setDescription(service.serviceDescription)
            setTag(tag)
            setOnDeleteListener {
                mViewModel.deleteService(service.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun addItemCombo(item: ComplexSelectedService) {
        val tag = createServiceTag(item.service.getServiceId())
        val view = SummaryItemViewCombo(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.setLayoutParams(layoutParams)
        view.setOnEditClickListener {
            //TODO: Later
        }
        view.setTag(tag)
        view.setOnDeleteListener {
            mViewModel.deleteService(item.service.id)
        }

        view.setInstruction(item.specialInstruction ?: "")

        item.selectedCustomisation?.let { hashMap ->
            for ((index, selectedPos) in hashMap) {
                val customisation = item.service.customisations[index]
                when (customisation) {
                    is BooleanCustomisation -> {
                        if (selectedPos == 0) {
                            //Yes
                            view.addOption("Yes", customisation.price)
                        } else if (selectedPos == 1) {
                            //No
                            view.addOption("No", 0f)
                        }
                    }

                    is MatrixCustomisation -> {
                        val matrixOptionItem = customisation.matrixOptions[selectedPos]
                        view.addOption(matrixOptionItem.value.toString(), matrixOptionItem.price)
                    }

                    is DropdownCustomisation -> {
                        val dropdownOptionItem = customisation.dropdownOptions[selectedPos]
                        view.addOption(dropdownOptionItem.label, dropdownOptionItem.price)
                    }

                    is NumberCustomisation -> {
                        val count = selectedPos + customisation.min
                        view.addOption((count).toString(), count * customisation.price)
                    }
                }
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

    private fun createCateTag(cateId: String) = "Cate" + cateId

    private fun createServiceTag(serviceId: Int) = "ServiceID${serviceId}"
}
