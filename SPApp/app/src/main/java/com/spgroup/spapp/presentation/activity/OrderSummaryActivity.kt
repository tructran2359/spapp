package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.view.SummaryItemView
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
        for (i in 0 until 3) {
            val view = SummaryItemView(this)
            view.setShowDivider(i != 2)

            //Temporarily hide views to see empty container
            view.visibility = View.INVISIBLE

            ll_item_container.addView(view)
        }

//        btn_summary.setOnClickListener {
//            //TODO: later
//        }

        btn_summary.isEnabled = false

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }
}
