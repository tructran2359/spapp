package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.presentation.fragment.UnsavedDataDialog
import com.spgroup.spapp.presentation.view.CounterView
import com.spgroup.spapp.presentation.view.CustomiseCounterView
import com.spgroup.spapp.presentation.viewmodel.CustomiseViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.extension.formatPrice
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.hideKeyboard
import com.spgroup.spapp.util.extension.updateVisibility
import kotlinx.android.synthetic.main.activity_customise.*

class CustomiseActivity : BaseActivity() {

    companion object {

        fun getLaunchIntent(
                context: Context,
                item: ServiceItem,
                initContent: CustomiseViewModel.Content? = null,
                isEdit: Boolean = false): Intent {

            val intent = Intent(context, CustomiseActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_IS_EDIT, isEdit)
            intent.putExtra(ConstUtils.EXTRA_SERVICE_ITEM, item)
            if (initContent != null) {
                intent.putExtra(ConstUtils.EXTRA_CONTENT, initContent)
            } else {
                intent.putExtra(ConstUtils.EXTRA_CONTENT, CustomiseViewModel.Content(paxCount = 1, riceCount = 1, instruction = "No beef and peanut. Low salt."))

            }
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mViewModel: CustomiseViewModel
    lateinit var mPaxView: CustomiseCounterView
    lateinit var mRiceView: CustomiseCounterView

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customise)

        val factory = ViewModelFactory.getInstance()

        mViewModel = ViewModelProviders.of(this, factory).get(CustomiseViewModel::class.java)


        with(mViewModel) {
            mServiceItem = intent.getSerializableExtra(ConstUtils.EXTRA_SERVICE_ITEM) as ServiceItemCombo
            mInitData = intent.getSerializableExtra(ConstUtils.EXTRA_CONTENT) as CustomiseViewModel.Content
            mCurrentInstruction = mInitData.instruction
            mIsEdit = intent.getBooleanExtra(ConstUtils.EXTRA_IS_EDIT, false)
            initData()

            paxCount.observe(this@CustomiseActivity, Observer {
                it?.let {
                    mPaxView.setCount(it)
                }
            })

            riceCount.observe(this@CustomiseActivity, Observer {
                it?.let {
                    mRiceView.setCount(it)
                }
            })

            estimatedPrice.observe(this@CustomiseActivity, Observer {
                it?.let {
                    tv_est_price.setText(it.formatPrice())
                }
            })

            if (mIsEdit) {

                isUpdated.observe(this@CustomiseActivity, Observer {
                    it?.let {
                        tv_bottom.setText(if (it) R.string.update_and_view_summary else R.string.back_to_view_summary)
                    }
                })
            }
        }

        initViews()
    }

    override fun onBackPressed() {
        if (mViewModel.mIsEdit) {
            mViewModel.isUpdated.value?.let {
                if (it) {
                    showConfirmDialog()
                } else {
                    super.onBackPressed()
                }
            }

        } else {
            super.onBackPressed()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {

        if (mViewModel.mIsEdit) {
            action_bar.setTitle(R.string.edit)

            tv_bottom.setText(R.string.back_to_view_summary)
            tv_bottom.setOnClickListener {

                et_instruction.hideKeyboard()

                with(mViewModel) {
                    val content = mInitData.copy(
                            paxCount = paxCount.value!!,
                            riceCount = riceCount.value!!,
                            instruction = mCurrentInstruction)
                    val intent = Intent()
                    intent.putExtra(ConstUtils.EXTRA_CONTENT, content)
                    intent.putExtra(ConstUtils.EXTRA_SERVICE_ID, mViewModel.mServiceItem.id)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            // In edit screen, set content of instruction here. Now just use dummy text
            et_instruction.setText(mViewModel.mInitData.instruction)
            et_instruction.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    var text = ""
                    if (p0 != null) {
                        text = p0.toString()
                    }
                    mViewModel.instructionChange(text)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }

        tv_name.setText(mViewModel.mServiceItem.name)
        tv_description.setText(mViewModel.mServiceItem.description)

        mPaxView = CustomiseCounterView(this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, getDimensionPixelSize(R.dimen.common_vert_large))
        mPaxView.layoutParams = layoutParams
        with(mPaxView) {
            setName("No. of Pax")
            setOption("[min. 1 pax]")
            setLimit(mViewModel.mInitData.paxMin, mViewModel.mInitData.paxMax)
            setCount(mViewModel.mInitData.paxCount)
            setOnCountChangeListener(object : CounterView.OnCountChangeListener {
                override fun onPlus() {
                    mViewModel.paxChange(true)
                }

                override fun onMinus() {
                    mViewModel.paxChange(false)
                }
            })
        }

        mRiceView = CustomiseCounterView(this)
        with(mRiceView) {
            setName("Plain Rice")
            setOption("[optional]")
            setLimit(mViewModel.mInitData.riceMin, mViewModel.mInitData.riceMax)
            setCount(mViewModel.mInitData.riceCount)
            setOnCountChangeListener(object : CounterView.OnCountChangeListener {
                override fun onPlus() {
                    mViewModel.riceChange(true)
                }

                override fun onMinus() {
                    mViewModel.riceChange(false)
                }
            })
        }

        ll_option_container.addView(mPaxView)
        ll_option_container.addView(mRiceView)

        action_bar.setOnBackPress {
            onBackPressed()
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
        bottom_button_container.updateVisibility(show)
        v_shadow.updateVisibility(show)
    }

    private fun showConfirmDialog() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prevDialog = supportFragmentManager.findFragmentByTag(ConstUtils.TAG_DIALOG)
        if (prevDialog != null) {
            fragmentTransaction.remove(prevDialog)
        }

        val newDialog = UnsavedDataDialog()
        with(newDialog) {
            setActions(
                    { super.onBackPressed() },
                    null
            )
            show(fragmentTransaction, ConstUtils.TAG_DIALOG)
        }
    }
}
