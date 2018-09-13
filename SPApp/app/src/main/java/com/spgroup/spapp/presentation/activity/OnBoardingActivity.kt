package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.SPApplication
import com.spgroup.spapp.presentation.adapter.OnBoardingAdapter
import com.spgroup.spapp.presentation.viewmodel.OnBoardingViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_on_boarding.*
import javax.inject.Inject

class OnBoardingActivity : BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, OnBoardingActivity::class.java)
            return intent
        }
    }

    private lateinit var mTncAnimAppear: Animation
    private lateinit var mNextAnimAppear: Animation
    private lateinit var mGetStartedAnimAppear: Animation

    private lateinit var mTncAnimDisappear: Animation
    private lateinit var mNextAnimDisappear: Animation
    private lateinit var mGetStartedAnimDisappear: Animation

    private lateinit var mAdapter: OnBoardingAdapter

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var mViewModel: OnBoardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_on_boarding)

        mViewModel = obtainViewModel(OnBoardingViewModel::class.java, ViewModelFactory.getInstance())

        setUpAnimation()
        setUpViews()
    }

    override fun onBackPressed() {
        val currentPosition = view_pager.currentItem
        if (currentPosition == 0) {
            super.onBackPressed()
        } else {
            view_pager.setCurrentItem(currentPosition - 1, true)
        }
    }

    private fun setUpViews() {

        setUpViewPager()

        tv_tnc.setUpClickableUnderlineSpan(R.string.on_boarding_tnc_formatted, R.string.tnc) {
            startActivity(PageActivity.getLaunchIntent(this@OnBoardingActivity, PageActivity.TYPE_TNC))
        }
    }

    private fun setUpAnimation() {
        mTncAnimAppear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        mTncAnimAppear.setUpAppear(tv_tnc)

        mNextAnimAppear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        mNextAnimAppear.setUpAppear(tv_next)

        mGetStartedAnimAppear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        mGetStartedAnimAppear.setUpAppear(tv_get_started)

        mTncAnimDisappear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)
        mTncAnimDisappear.setUpDisappear(tv_tnc)

        mNextAnimDisappear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)
        mNextAnimDisappear.setUpDisappear(tv_next)

        mGetStartedAnimDisappear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)
        mGetStartedAnimDisappear.setUpDisappear(tv_get_started)
    }

    private fun setUpViewPager() {
        mAdapter = OnBoardingAdapter(supportFragmentManager)
        view_pager.adapter = mAdapter
        view_pager.offscreenPageLimit = mAdapter.count

        pager_indicator.setViewPager(view_pager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == mAdapter.count - 1) {
                    tv_tnc.showWithAnimation(mTncAnimAppear)
                    tv_get_started.showWithAnimation(mGetStartedAnimAppear)
                    tv_next.hideWithAnimation(mNextAnimDisappear)
                } else {
                    tv_tnc.hideWithAnimation(mTncAnimDisappear)
                    tv_get_started.hideWithAnimation(mGetStartedAnimDisappear)
                    tv_next.showWithAnimation(mNextAnimAppear)
                }

                mViewModel.notifyPageChanged(position)
            }
        })
        mViewModel.notifyPageChanged(0)

        tv_next.setOnClickListener {
            val currentPos = view_pager.currentItem
            view_pager.setCurrentItem(currentPos + 1, true)
        }

        tv_get_started.setOnClickListener {
            SPApplication.mAppConfig.setOnBoadingShown(true)
            startActivity(HomeActivity.getLaunchIntent(this@OnBoardingActivity))
            finish()
        }
    }
}