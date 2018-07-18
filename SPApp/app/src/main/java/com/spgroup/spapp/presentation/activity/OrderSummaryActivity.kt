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

        btn_summary.isEnabled = true
        btn_summary.setText(getString(R.string.submit_request))
        btn_summary.setCount(1)
        btn_summary.setEstPrice(0.01f)

        iv_back.setOnClickListener {
            onBackPressed()
        }

        val list = listOf("11AM - 12PM", "12PM - 2PM", "2PM - 4PM")
        val adapter = ArrayAdapter(this, R.layout.layout_preferred_time, list)
        adapter.setDropDownViewResource(R.layout.layout_preferred_time_dropdown)

        spinner_preferred_time.adapter = adapter
    }
}
