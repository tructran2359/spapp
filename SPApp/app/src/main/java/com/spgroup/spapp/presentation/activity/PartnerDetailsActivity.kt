package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.CategoryPagerAdapter
import com.spgroup.spapp.presentation.adapter.PartnerImagesAdapter
import com.spgroup.spapp.presentation.viewmodel.SupplierDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.loadAnimation
import com.spgroup.spapp.util.extension.setOnGlobalLayoutListener
import com.spgroup.spapp.util.extension.setUpMenuActive
import com.spgroup.spapp.util.extension.setUpMenuInactive
import kotlinx.android.synthetic.main.activity_partner_details.*

class PartnerDetailsActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, PartnerDetailsActivity::class.java)
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

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_details)

        initAnimations()

        setUpViews()

        // This is demo for using ViewModel
        val factory = ViewModelFactory.getInstance()
        val viewmodel = ViewModelProviders.of(this, factory).get(SupplierDetailsViewModel::class.java)
        with(viewmodel) {

            serviceCategories.observe(this@PartnerDetailsActivity, Observer {
                // do something with serviceCategories
                doLogD(msg = "Size: ${it?.size}")
                mCategoryAdapter.setData(it)
                setUpTabLayout()
            })

            error.observe(this@PartnerDetailsActivity, Observer {
                // do something with error
                doLogE(msg = "Error: ${it?.message}")
            })

            loadServices(-1)
        }
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
            tvContent.setText(mCategoryAdapter.getPageTitle(i))
            if (i == 0) {
                tvContent.setUpMenuActive()
            } else {
                tvContent.setUpMenuInactive()
            }
            val tab = tab_layout.getTabAt(i)
            tab?.setCustomView(customView)
        }
    }

    private fun setUpHeroSection() {
        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_information.setOnClickListener {
            startActivity(PartnerInformationActivity.getLaunchIntent(this@PartnerDetailsActivity))
        }

        rl_hero_section.setOnGlobalLayoutListener {
            val width = rl_hero_section.width
            val height = (width * 9f / 16f).toInt()
            val layoutParams = rl_hero_section.layoutParams
            layoutParams.height = height
            rl_hero_section.layoutParams = layoutParams
        }

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
        rl_summary_container.visibility = visibility
        v_shadow.visibility = visibility
    }

}
