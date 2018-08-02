package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R

class HomeAcknowledgementActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context) = Intent(context, HomeAcknowledgementActivity::class.java)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_ack)
    }
}