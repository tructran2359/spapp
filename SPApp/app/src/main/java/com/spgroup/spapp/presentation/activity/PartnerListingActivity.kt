package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R

class PartnerListingActivity: BaseActivity() {

    companion object {

        fun getLaunchIntent(context: Context) : Intent {
            val intent = Intent(context, PartnerListingActivity::class.java)
            return intent
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_listing)
    }
}