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
        tv_view.setOnClickListener {
            moveToPartnerDetails()
        }
    }

    fun moveToPartnerDetails() {
        val intent = PartnerDetailsActivity.getLaunchIntent(this)
        startActivity(intent)
    }
}
