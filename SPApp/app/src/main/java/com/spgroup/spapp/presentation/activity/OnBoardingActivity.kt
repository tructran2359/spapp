package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.OnBoardingAdapter
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, OnBoardingActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        view_pager.adapter = OnBoardingAdapter(supportFragmentManager)
        pager_indicator.setViewPager(view_pager)
    }
}