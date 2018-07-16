package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.view.IndicatorTextView
import kotlinx.android.synthetic.main.activity_partner_information.*

class PartnerInformationActivity : BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, PartnerInformationActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_information)

        initViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {
        val list = listOf(
                "Dry Cleaning",
                "Curtain Cleaning",
                "Enjoy free collection & delivery over $30",
                "Special request available")

        for (str in list) {
            val view = IndicatorTextView(this, str)
            ll_service_container.addView(view)
        }

        iv_close.setOnClickListener {
            onBackPressed()
        }
    }
}
