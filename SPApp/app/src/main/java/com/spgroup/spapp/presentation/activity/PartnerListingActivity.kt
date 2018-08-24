package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import com.bumptech.glide.Glide
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.presentation.adapter.PartnerAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerListingViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_partner_listing.*
import org.jetbrains.anko.longToast
import kotlin.math.max

class PartnerListingActivity : BaseActivity() {

    companion object {

        fun getLaunchIntent(context: Context, topLevelCategory: TopLevelCategory) =
                Intent(context, PartnerListingActivity::class.java).apply {
                    putExtra(ConstUtils.EXTRA_TOP_LEVEL_CATEGORY, topLevelCategory)
                }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mViewModel: PartnerListingViewModel
    lateinit var mPartnerListAdapter: PartnerAdapter

    private var mBannerHeight = 0
    private var mActionBarHeight: Int = 0
    private var mScreenWidth: Int = 0
    private var mInitTextSize = 0f
    private var mInitTitleWidth = 0
    private var mHorzMargin = 0

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_listing)
        initViews()

        val cat = intent.getSerializableExtra(ConstUtils.EXTRA_TOP_LEVEL_CATEGORY) as TopLevelCategory
        mViewModel = obtainViewModel(PartnerListingViewModel::class.java, ViewModelFactory.getInstance())
        mViewModel.setInitialData(cat)

        with(mViewModel) {
            topLevelCategory.observe(this@PartnerListingActivity, Observer {
                it?.let { updateBanner(it) }
            })
            partnerListingItems.observe(this@PartnerListingActivity, Observer {
                mPartnerListAdapter.setData(it)
            })
            loadPartnerListing()
        }
    }

    private fun updateBanner(topLevelCategory: TopLevelCategory) {
        tv_title.text = topLevelCategory.name
        Glide.with(this@PartnerListingActivity)
                .load(topLevelCategory.banner.toFullUrl())
                .into(iv_banner)
    }

    ///////////////////////////////////////////////////////////////////////////
    // PartnerAdapter.OnItemClickListener
    ///////////////////////////////////////////////////////////////////////////

    private fun onPartnerListingItemClick(view: View, itemData: PartnersListingItem, position: Int) {
        when (itemData) {
            is Partner -> {
                val intent = getPartnerDetailIntent(itemData.uen, itemData.partnerType)
                startActivity(intent)
            }

            is Promotion -> longToast("Promotion 's coming soon")
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun initViews() {
        val displayMetrics = getDisplayMetrics()
        mBannerHeight = (displayMetrics.widthPixels * 9f / 16f).toInt()
        mActionBarHeight = getDimensionPixelSize(R.dimen.action_bar_height)
        mScreenWidth = displayMetrics.widthPixels
        mInitTextSize = tv_title.textSize
        mHorzMargin = getDimensionPixelSize(R.dimen.common_horz_large)

        tv_title.setOnGlobalLayoutListener {
            mInitTitleWidth = tv_title.width
        }

        app_bar_layout.setLayoutParamsHeight(mBannerHeight)
        fl_title_container.setLayoutParamsHeight(mBannerHeight)

        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
                appBarLayout?.let {
                    val currentScroll = appBarLayout.bottom - mActionBarHeight
                    val maxScroll = appBarLayout.totalScrollRange
                    val percentage = currentScroll.toFloat() / maxScroll.toFloat()

                    fl_title_container.setLayoutParamsHeight(appBarLayout.bottom)
                    if (currentScroll <= mActionBarHeight) {
                        v_title_background.alpha = 1f
                    } else {
                        v_title_background.alpha = 1f - percentage
                    }
                    val scaledTextSize = mInitTextSize * max(percentage, 0.7f)
                    tv_title.run {
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, scaledTextSize)
                        if (mInitTitleWidth != 0) {
                            val diffSize = mScreenWidth - mInitTitleWidth - 2 * mHorzMargin
                            val realSize = mInitTitleWidth + (diffSize * (1f- percentage))
                            setLayoutParamsWidth(realSize.toInt())
                        }

                    }
                    doLogD("Scroll", "text: ${tv_title.width} + width: $mInitTitleWidth + max: $maxScroll + cur: $currentScroll + per: $percentage")

                }
            }
        })

        iv_back.setOnClickListener {
            onBackPressed()
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        mPartnerListAdapter = PartnerAdapter { view, itemData, position ->
            onPartnerListingItemClick(view, itemData, position)
        }
        recycler_view.adapter = mPartnerListAdapter
    }
}