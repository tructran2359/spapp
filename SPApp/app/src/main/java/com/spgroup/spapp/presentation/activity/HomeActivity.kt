package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.core.view.isGone
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.domain.model.TopLevelPromotion
import com.spgroup.spapp.presentation.adapter.HomeMenuItemAdapter
import com.spgroup.spapp.presentation.adapter.HomeMerchantAdapter
import com.spgroup.spapp.presentation.adapter.HomePromotionAdapter
import com.spgroup.spapp.presentation.adapter.item_decoration.HomeMerchantItemtDecoration
import com.spgroup.spapp.presentation.adapter.item_decoration.VerticalItemDecoration
import com.spgroup.spapp.presentation.view.TopLeverCateGroupView
import com.spgroup.spapp.presentation.viewmodel.HomeViewModel
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.menu_home.*
import org.jetbrains.anko.longToast
import javax.inject.Inject

open class HomeActivity :
        BaseActivity(),
        HomePromotionAdapter.OnPromotionClickListener,
        HomeMerchantAdapter.OnMerchantClickListener {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mMenu: SlidingMenu
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mMenuAdapter: HomeMenuItemAdapter
    private lateinit var mPromotionAdapter: HomePromotionAdapter
    private lateinit var mMerchantAdapter: HomeMerchantAdapter

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_home)
        setupViewModel()
        subscribeUI()
        setupViews()
    }

    private fun setupViewModel() {
        mViewModel = obtainViewModel(HomeViewModel::class.java, vmFactory)
        mViewModel.getInitData()
    }

    ///////////////////////////////////////////////////////////////////////////
    // HomePromotionAdapter.OnPromotionClickListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onPromotionClick(promotion: TopLevelPromotion, position: Int) {
        startActivityWithCheckingInternet(getPartnerDetailIntent(promotion.partnerId, promotion.partnerType))
    }

    ///////////////////////////////////////////////////////////////////////////
    // HomeMerchantAdapter.OnMerchantClickListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onMerchantClick(topLevelFeaturedPartner: TopLevelFeaturedPartner, position: Int) {
        startActivityWithCheckingInternet(getPartnerDetailIntent(topLevelFeaturedPartner.id, topLevelFeaturedPartner.partnerType))
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setupViews() {
        setUpMenu()

        iv_menu.setOnClickListener {
            mMenu.toggle()
        }

        iv_close.setOnClickListener {
            rl_noti_container.visibility = View.GONE
        }

        cate_group_view.setOnCategoryClickListener(object : TopLeverCateGroupView.OnCategoryClickListener {
            override fun onCategoryClick(position: Int) {
                this@HomeActivity.onCategoryClick(position)
            }
        })

        setupPromotions()
        setupMerchants()
    }

    private fun setupPromotions() {
        val promotionLayoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false)
        recycler_view_promotions.layoutManager = promotionLayoutManager

        val displayMetrics = getDisplayMetrics()
        val screenWidth = displayMetrics.widthPixels
        mPromotionAdapter = HomePromotionAdapter(screenWidth, this)
        recycler_view_promotions.adapter = mPromotionAdapter
    }

    private fun setupMerchants() {
        val merchantLayoutManager = GridLayoutManager(
                this,
                ConstUtils.HOME_MERCHANT_ROW_COUNT,
                GridLayoutManager.HORIZONTAL,
                false)
        recycler_view_merchant.layoutManager = merchantLayoutManager
        recycler_view_merchant.addItemDecoration(HomeMerchantItemtDecoration(ConstUtils.HOME_MERCHANT_ROW_COUNT))
        mMerchantAdapter = HomeMerchantAdapter(this)
        recycler_view_merchant.adapter = mMerchantAdapter
    }

    private fun subscribeUI() {
        with(mViewModel) {
            topLevelVariabes.observe(this@HomeActivity, Observer {
                it?.let {
                    tv_home_header.text = it.headerLine1 + if (it.headerLine2.isEmpty()) "" else "\n${it.headerLine2}"
                    tv_home_subheader.text = it.subHeader
                    tv_notification.text = it.alert
                    rl_noti_container.isGone = it.alert.isEmpty()
                }
            })

            listTopLevelCate.observe(this@HomeActivity, Observer {
                it?.let {
                    cate_group_view.setListCategory(it)
                    mMenuAdapter.setData(it)
                }
            })

            listTopLevelPromotion.observe(this@HomeActivity, Observer {
                it?.let {
                    mPromotionAdapter.setData(it)
                }
            })

            listTopLevelPartner.observe(this@HomeActivity, Observer {
                it?.let {
                    mMerchantAdapter.setData(it)
                }
            })

            error.observe(this@HomeActivity, Observer {
                this@HomeActivity.longToast("Error ${it?.message}")
            })
        }
    }

    private fun setUpMenu() {
        mMenu = SlidingMenu(this)

        with(mMenu) {
            touchModeAbove = SlidingMenu.TOUCHMODE_NONE
            setShadowWidthRes(R.dimen.common_spacing_x3)
            setShadowDrawable(R.drawable.gradient_menu_shadow)
            setBehindOffsetRes(R.dimen.menu_margin_right)
            setMenu(R.layout.menu_home)
            attachToActivity(this@HomeActivity, SlidingMenu.SLIDING_CONTENT)
        }

        tv_app_version.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME))

        ll_about_us_container.setOnClickListener {
            startActivity(PageActivity.getLaunchIntent(this@HomeActivity, PageActivity.TYPE_ABOUT))
        }

        rl_tnc_container.setOnClickListener {
            startActivity(PageActivity.getLaunchIntent(this@HomeActivity, PageActivity.TYPE_TNC))
        }

        rl_ack_container.setOnClickListener {
            startActivity(PageActivity.getLaunchIntent(this@HomeActivity, PageActivity.TYPE_ACK))
        }

        mMenuAdapter = HomeMenuItemAdapter()
        mMenuAdapter.setOnItemClickListener(object : HomeMenuItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                this@HomeActivity.onCategoryClick(position)
            }
        })
        recycler_view_home_menu.layoutManager = LinearLayoutManager(this)
        val space = getDimensionPixelSize(R.dimen.home_menu_item_distance)
        recycler_view_home_menu.addItemDecoration(VerticalItemDecoration(space))
        recycler_view_home_menu.adapter = mMenuAdapter
    }

    private fun onCategoryClick(position: Int) {
        val category = mViewModel.getCategoryByIndex(position)
        category?.let {
            startActivityWithCheckingInternet(PartnerListingActivity.getLaunchIntent(this@HomeActivity, category))
        }
    }

}
