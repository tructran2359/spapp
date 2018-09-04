package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.activity_no_internet.*

class NoInternetActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, NoInternetActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)

        setUpViews()
    }

    private fun setUpViews() {
        tv_back.setOnClickListener {
            onBackPressed()
        }
    }
}