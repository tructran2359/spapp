package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.presentation.adapter.PartnerAdapter
import com.spgroup.spapp.presentation.adapter.item_decoration.VerticalItemDecoration
import com.spgroup.spapp.presentation.viewmodel.PartnerListingViewModel
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_partner_listing.*
import javax.inject.Inject
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

    @Inject lateinit var vmFactory: ViewModelProvider.Factory

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

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_partner_listing)
        initViews()

        val cat = intent.getSerializableExtra(ConstUtils.EXTRA_TOP_LEVEL_CATEGORY) as TopLevelCategory
        mViewModel = obtainViewModel(PartnerListingViewModel::class.java, vmFactory)
        subscribeUI()
        mViewModel.setInitialData(cat)
        mViewModel.loadPartnerListing()
    }

    private fun subscribeUI() {
        with(mViewModel) {
            topLevelCategory.observe(this@PartnerListingActivity, Observer {
                it?.let { updateBanner(it) }
            })

            partnerListingItems.observe(this@PartnerListingActivity, Observer {
                // to simulate long text on promotion
//                val testList = it?.toMutableList() ?: mutableListOf()
//                val promotionShort = Promotion(
//                        "test",
//                        "This is very long text.This is very long text.This is very long text.This is very long text.This is very long text.This is very long text.This is very long text.This is very long text.This is very long text.This is very long text.",
//                        "Short Name",
//                        "12",
//                        "13",
//                        "1234",
//                        "cart")
//
//                val promotionLong = promotionShort.copy(partnerName = "Long Name Long Name Long Name Long Name Long Name ")
//                testList.add(promotionShort)
//                testList.add(promotionLong)
//                mPartnerListAdapter.setData(testList)

                mPartnerListAdapter.setData(it)
            })

            error.observe(this@PartnerListingActivity, Observer {
                it?.let { throwable ->
                    doLogE("Error", "Error: $throwable with message: ${throwable.message}")

                    // Show error screen and finish so when press back on error screen, app will back to Home
                    startActivity(ApiErrorActivity.getLaunchIntent(this@PartnerListingActivity))
                    finish()
                }
            })
        }
    }

    private fun updateBanner(topLevelCategory: TopLevelCategory) {
        tv_title.text = topLevelCategory.name
        iv_banner.loadImage(topLevelCategory.banner.toFullUrl())
    }

    ///////////////////////////////////////////////////////////////////////////
    // PartnerAdapter.OnItemClickListener
    ///////////////////////////////////////////////////////////////////////////

    private fun onPartnerListingItemClick(view: View, itemData: PartnersListingItem, position: Int) {
        when (itemData) {
            is Partner -> {
                val intent = getPartnerDetailIntent(itemData.uen, itemData.partnerType)
                startActivityWithCheckingInternet(intent)
            }

            is Promotion -> {
                val intent = getPartnerDetailIntent(itemData.partnerId, itemData.partnerType)
                startActivityWithCheckingInternet(intent)
            }
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
        val space = getDimensionPixelSize(R.dimen.common_vert_small)
        recycler_view.addItemDecoration(VerticalItemDecoration(space))
        mPartnerListAdapter = PartnerAdapter { view, itemData, position ->
            onPartnerListingItemClick(view, itemData, position)
        }
        recycler_view.adapter = mPartnerListAdapter
    }
}