package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.OnBoardingAdapter
import com.spgroup.spapp.util.extension.setUpClickableUnderlineSpan
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

        setUpViews()
    }

    private fun setUpViews() {

        view_pager.adapter = OnBoardingAdapter(supportFragmentManager)
        pager_indicator.setViewPager(view_pager)

        tv_tnc.setUpClickableUnderlineSpan(R.string.on_boarding_tnc_formatted, R.string.tnc) {
            startActivity(PageActivity.getLaunchIntent(this@OnBoardingActivity, PageActivity.TYPE_TNC))
        }
    }
}