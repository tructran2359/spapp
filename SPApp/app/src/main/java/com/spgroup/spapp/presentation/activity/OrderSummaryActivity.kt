package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.presentation.view.ValidationInputView
import com.spgroup.spapp.util.extension.toast
import kotlinx.android.synthetic.main.activity_order_summary.*

class OrderSummaryActivity : BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, OrderSummaryActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        initViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {

        action_bar.setTitle(R.string.summary)

        with(summary_view_combo) {

            addOption("Lunch for 1 pax", 165f)

            addOption("1 Plain Rice", 20f)

            setOnEditClickListener {

                //Create dummy data:
                val service = ServiceItemCombo (
                        "3 Dishes Plus 1 Soup Meal Set",
                        165f,
                        "month",
                        "Weekdays only. Island-wide delivery. Packed in microwavable containers only.",
                        false
                )
                val intent = CustomiseActivity.getLaunchIntent(
                        this@OrderSummaryActivity,
                        service,
                        true)
                this@OrderSummaryActivity.startActivity(intent)
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

        validation_email.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        validation_contact_no.setInputType(InputType.TYPE_CLASS_PHONE)
        validation_postal_code.setInputType(InputType.TYPE_CLASS_NUMBER)

        with(btn_summary) {
            isEnabled = true
            setText(getString(R.string.submit_request))
            setCount(1)
            setEstPrice(0.01f)
            setOnClickListener {
                var invalidCount = 0
                var invalidView : ValidationInputView? = null

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
                        invalidView = it
                    }
                }
                if (invalidCount == 0) {
                    startActivity(AcknowledgementActivity.getLaunchIntent(this@OrderSummaryActivity))
                } else {
                    toast("Invalid Count: $invalidCount")
                }
            }
        }

        action_bar.setOnBackPress {
            onBackPressed()
        }

        val list = listOf("11AM - 12PM", "12PM - 2PM", "2PM - 4PM")
        val adapter = ArrayAdapter(this, R.layout.layout_preferred_time, list)
        adapter.setDropDownViewResource(R.layout.layout_preferred_time_dropdown)

        spinner_preferred_time.adapter = adapter
    }
}
