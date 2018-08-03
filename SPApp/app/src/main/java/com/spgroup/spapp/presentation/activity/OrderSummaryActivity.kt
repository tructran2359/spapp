package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.presentation.adapter.PreferredTimeAdapter
import com.spgroup.spapp.presentation.view.ValidationInputView
import com.spgroup.spapp.presentation.viewmodel.CustomiseViewModel
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

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        initAnimations()
        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_EDIT && resultCode == Activity.RESULT_OK) {
            data?.let {
                val content = data.getSerializableExtra(ConstUtils.EXTRA_CONTENT) as CustomiseViewModel.Content
                summary_view_combo.setDummyData(content)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

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

        with(summary_view_combo) {

            val dummy = CustomiseViewModel.Content(paxCount = 1, riceCount = 1, instruction = "No beef and peanut. Low salt.")
            setDummyData(dummy)

            setOnEditClickListener {

                //Create dummy data:
                val service = ServiceItemCombo (
                        "3 Dishes Plus 1 Soup Meal Set",
                        165f,
                        "month",
                        "Weekdays only. Island-wide delivery. Packed in microwavable containers only.",
                        false
                )

                val dummyData = getDummyData()

                val intent = CustomiseActivity.getLaunchIntent(
                        this@OrderSummaryActivity,
                        service,
                        dummyData,
                        true)
                this@OrderSummaryActivity.startActivityForResult(intent, RC_EDIT)
            }
        }

        summary_view_1.initData(1, 10, 1)

        with(summary_view_2) {
            setName("Load Wash / kg")
            initData(5, 10, 5)
        }

        with(summary_view_3) {
            setName("Load Wash / kg")
            initData(1, 10, 1)
        }

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

    private fun showErrorView(show: Boolean) {
        if (show && rl_error_cointainer.visibility != View.VISIBLE) {
            rl_error_cointainer.startAnimation(mAnimAppear)
        } else if (!show && rl_error_cointainer.visibility == View.VISIBLE) {
            rl_error_cointainer.startAnimation(mAnimDisappear)
        }
    }
}
