package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.CategoryPagerAdapter
import com.spgroup.spapp.presentation.adapter.PartnerImagesAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_partner_details.*

class PartnerDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    ///////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////

    companion object {

        fun getLaunchIntent(context: Context, partnerUEN: String): Intent {
            val intent = Intent(context, PartnerDetailsActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_PARTNER_UEN, partnerUEN)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mImageAdapter: PartnerImagesAdapter
    lateinit var mCategoryAdapter: CategoryPagerAdapter
    lateinit var mAnimationAppear: Animation
    lateinit var mAnimationDisappear: Animation
    private var mActionBarHeight: Int = 0

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_details)

        mActionBarHeight = getDimensionPixelSize(R.dimen.action_bar_height)

        initAnimations()

        setUpViews()

        setupViewModel()
    }

    private fun setupViewModel() {
        val partnerUEN = intent.getStringExtra(ConstUtils.EXTRA_PARTNER_UEN)
        doLogD("Partner", "onCreate partner: ${partnerUEN ?: "null"}")
        // This is demo for using ViewModel
        val viewmodel = obtainViewModel(PartnerDetailsViewModel::class.java, ViewModelFactory.getInstance())
                .apply { this.partnerUEN =  partnerUEN}
        with(viewmodel) {

            partnerDetails.observe(this@PartnerDetailsActivity, Observer {
                mCategoryAdapter.setData(it?.categories)
                setUpTabLayout()
            })

            selectedCount.observe(this@PartnerDetailsActivity, Observer {
                doLogD("Test", "Selected $it item")
                it?.let {
                    // Temporarily hide this line coz updating total count textview is not in this task
                    //                    btn_summary.setCount(it)
                    if (it != 0 && ll_summary_section.visibility == View.GONE) {
                        showSummaryButton(true)
                    } else if (it == 0 && ll_summary_section.visibility == View.VISIBLE) {
                        showSummaryButton(false)
                    }
                }
            })

            error.observe(this@PartnerDetailsActivity, Observer {
                // do something with error
                doLogE(msg = "Error: ${it?.message}")
            })

            loadServices()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // AppBarLayout
    ///////////////////////////////////////////////////////////////////////////

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        if (appBarLayout == null) return
        val currentScroll = appBarLayout.bottom
        val mMaxScroll = appBarLayout.totalScrollRange
        val percentage = currentScroll.toFloat() / (mMaxScroll).toFloat()

        val layoutParam = rl_top_button_container.layoutParams
        if (currentScroll <= mActionBarHeight) {
            v_background_color.alpha = 1f
            fl_info_container.visibility = View.GONE
            layoutParam.height = mActionBarHeight
        } else {
            v_background_color.alpha = 1f - percentage
            fl_info_container.visibility = View.VISIBLE
            fl_info_container.alpha = percentage
            layoutParam.height = currentScroll
        }

        rl_top_button_container.layoutParams = layoutParam
        val scale = if (percentage <= 0.7f) 0.7f else percentage
        tv_partner_name.scaleX = scale
        tv_partner_name.scaleY = scale
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initAnimations() {
        mAnimationAppear = loadAnimation(R.anim.anim_slide_up)
        mAnimationAppear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                updateVisibility(true)
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })

        mAnimationDisappear = loadAnimation(R.anim.anim_slide_down)
        mAnimationDisappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                updateVisibility(false)
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    private fun setUpViews() {
        setUpHeroSection()

        setUpFormSection()

        setUpSummarySection()
    }

    private fun setUpFormSection() {
        mCategoryAdapter = CategoryPagerAdapter(supportFragmentManager)

        pager_forms.offscreenPageLimit = 3
        pager_forms.adapter = mCategoryAdapter

        tab_layout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //Do nothing
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val textView = customView?.findViewById<TextView>(R.id.tv_content)
                textView?.setUpMenuInactive()
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val textView = customView?.findViewById<TextView>(R.id.tv_content)
                textView?.setUpMenuActive()
            }

        })

    }

    private fun setUpTabLayout() {
        tab_layout.setupWithViewPager(pager_forms)
        for (i in 0 until tab_layout.tabCount) {
            val customView = LayoutInflater.from(this).inflate(R.layout.view_custom_service_cate_tab, null, false)
            val tvContent = customView.findViewById<TextView>(R.id.tv_content)
            tvContent.text = mCategoryAdapter.getPageTitle(i)
            if (i == 0) {
                tvContent.setUpMenuActive()
            } else {
                tvContent.setUpMenuInactive()
            }
            val tab = tab_layout.getTabAt(i)
            tab?.customView = customView
        }
    }

    private fun setUpHeroSection() {
        fl_back_container.setOnClickListener {
            onBackPressed()
        }

        fl_info_container.setOnClickListener {
            startActivity(PartnerInformationActivity.getLaunchIntent(this@PartnerDetailsActivity))
        }

        rl_hero_section.setOnGlobalLayoutListener {
            val width = rl_hero_section.width
            val height = (width * 9f / 16f).toInt()
            val layoutParams = rl_hero_section.layoutParams
            val layoutParamsMask = rl_top_button_container.layoutParams
            layoutParams.height = height
            layoutParamsMask.height = height
            rl_hero_section.layoutParams = layoutParams
            rl_top_button_container.layoutParams = layoutParamsMask
        }

        appbar.addOnOffsetChangedListener(this)

        mImageAdapter = PartnerImagesAdapter(supportFragmentManager)
        pager_images.offscreenPageLimit = 3
        pager_images.adapter = mImageAdapter

        pager_indicator.setViewPager(pager_images)
    }

    private fun setUpSummarySection() {
        ll_summary_section.visibility = View.GONE

        btn_summary.setOnClickListener {
            val intent = OrderSummaryActivity.getLaunchIntent(this)
            startActivity(intent)
        }
    }

    private fun showSummaryButton(show: Boolean) {
        val animation = if (show) mAnimationAppear else mAnimationDisappear
        ll_summary_section.startAnimation(animation)
    }

    private fun updateVisibility(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        ll_summary_section.visibility = visibility
        val params = pager_forms.layoutParams as LinearLayout.LayoutParams
        params.bottomMargin = if (show) getDimensionPixelSize(R.dimen.bottom_button_total_height) else 0
        pager_forms.layoutParams = params
    }

}
