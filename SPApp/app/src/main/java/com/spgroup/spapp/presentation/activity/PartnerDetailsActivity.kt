package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.activity_partner_details.*

class PartnerDetailsActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, PartnerDetailsActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_details)

        setupViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun setupViews() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

}
