package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

        btn_summary.isEnabled = false

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }
}
