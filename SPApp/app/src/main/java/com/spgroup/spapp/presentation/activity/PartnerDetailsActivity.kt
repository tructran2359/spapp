package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.widget.TextView
import com.spgroup.spapp.R
import com.spgroup.spapp.extension.setOnGlobalLayoutListener
import com.spgroup.spapp.extension.setUpMenuActive
import com.spgroup.spapp.extension.setUpMenuInactive
import com.spgroup.spapp.presentation.adapter.CategoryPagerAdapter
import com.spgroup.spapp.presentation.adapter.PartnerImagesAdapter
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

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_details)

        setUpViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun setUpViews() {
        setUpHeroSection()

        setUpFormSection()

    }

    private fun setUpFormSection() {
        mCategoryAdapter = CategoryPagerAdapter(supportFragmentManager)

        // Fake data:
        val data = listOf(
                "Dry Clean",
                "Wash & Press",
                "Press Only",
                "Wash & Fold",
                "Curtains & Carpets")
        mCategoryAdapter.setData(data)

        pager_forms.offscreenPageLimit = 3
        pager_forms.adapter = mCategoryAdapter

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

        tab_layout.addOnTabSelectedListener(object :  TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
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

    private fun setUpHeroSection() {
        iv_back.setOnClickListener {
            onBackPressed()
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

}
