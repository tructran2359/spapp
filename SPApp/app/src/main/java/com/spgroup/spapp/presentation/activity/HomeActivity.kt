package com.spgroup.spapp.presentation.activity

import android.os.Bundle
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setupViews() {
        view_button.setOnClickListener {
            moveToPartnerDetails()
        }

        view_partner_listing.setOnClickListener {
            val intent = PartnerListingActivity.getLaunchIntent(this@HomeActivity)
            startActivity(intent)
        }
    }

    fun moveToPartnerDetails() {
        val intent = PartnerDetailsActivity.getLaunchIntent(this)
        startActivity(intent)
    }
}
