package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R

class TermsAndConditionsActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context) = Intent(context, TermsAndConditionsActivity::class.java)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tnc)
    }
}