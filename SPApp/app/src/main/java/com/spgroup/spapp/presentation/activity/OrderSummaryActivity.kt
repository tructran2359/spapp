package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.spgroup.spapp.R
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

        summary_view_1.initData(1, 10, 1)

        summary_view_2.setName("Load Wash / kg")
        summary_view_2.initData(5, 10, 5)

        summary_view_3.setName("Load Wash / kg")
        summary_view_3.initData(1, 10, 1)

        btn_summary.isEnabled = true
        btn_summary.setText(getString(R.string.submit_request))
        btn_summary.setCount(1)
        btn_summary.setEstPrice(0.01f)
        btn_summary.setOnClickListener {
            startActivity(AcknowledgementActivity.getLaunchIntent(this@OrderSummaryActivity))
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
