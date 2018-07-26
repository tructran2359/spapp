package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.spgroup.spapp.util.extension.toast
import kotlinx.android.synthetic.main.activity_customise.*

class CustomiseActivity : BaseActivity() {

    companion object {

        fun getLaunchIntent(context: Context, item: ServiceItem, isEdit: Boolean = false): Intent {
            val intent = Intent(context, CustomiseActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_IS_EDIT, isEdit)
            intent.putExtra(ConstUtils.EXTRA_SERVICE_ITEM, item)
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
        mViewModel.mServiceItem = intent.getSerializableExtra(ConstUtils.EXTRA_SERVICE_ITEM) as ServiceItemCombo
        mViewModel.mIsEdit = intent.getBooleanExtra(ConstUtils.EXTRA_IS_EDIT, false)

        with(mViewModel) {
            if (mIsEdit) {
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

                isUpdated.observe(this@CustomiseActivity, Observer {
                    it?.let {
                        tv_bottom.setText(if (it) R.string.update_and_view_summary else R.string.back_to_view_summary)
                    }
                })

                estimatedPrice.observe(this@CustomiseActivity, Observer {
                    it?.let {
                        tv_est_price.setText(it.formatPrice())
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
                    showConfirmPopUp()
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

        tv_bottom.setOnClickListener {
            this.toast("CLICKED")
        }

        action_bar.setOnBackPress {
            onBackPressed()
        }
    }

    private fun showConfirmPopUp() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prevDialog = supportFragmentManager.findFragmentByTag(ConstUtils.TAG_DIALOG)
        if (prevDialog != null) {
            fragmentTransaction.remove(prevDialog)
        }

        val newDialog = UnsavedDataDialog()
        newDialog.setActions(
                { super.onBackPressed() },
                null
        )
        newDialog.show(fragmentTransaction, ConstUtils.TAG_DIALOG)
    }
}
