package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.HomeMenuItemAdapter
import com.spgroup.spapp.presentation.adapter.HomeMerchantAdapter
import com.spgroup.spapp.presentation.adapter.HomePromotionAdapter
import com.spgroup.spapp.presentation.adapter.item_decoration.HomeMenuItemDecoration
import com.spgroup.spapp.presentation.adapter.item_decoration.HomeMerchantItemtDecoration
import com.spgroup.spapp.presentation.view.TopLeverCateGroupView
import com.spgroup.spapp.presentation.viewmodel.HomeViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.getDisplayMetrics
import com.spgroup.spapp.util.extension.toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.menu_home.*

class HomeActivity : BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context) : Intent {
            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mMenu: SlidingMenu
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mMenuAdapter: HomeMenuItemAdapter

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        subcribeUI()

        setupViews()
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
        recycler_view_promotions.adapter = HomePromotionAdapter(this, screenWidth)
    }

    private fun setupMerchants() {
        val merchantLayoutManager = GridLayoutManager(
                this,
                ConstUtils.HOME_MERCHANT_ROW_COUNT,
                GridLayoutManager.HORIZONTAL,
                false)
        recycler_view_merchant.layoutManager = merchantLayoutManager
        recycler_view_merchant.addItemDecoration(HomeMerchantItemtDecoration(ConstUtils.HOME_MERCHANT_ROW_COUNT))
        recycler_view_merchant.adapter = HomeMerchantAdapter()
    }

    private fun subcribeUI() {
        val factory = ViewModelFactory.getInstance()
        mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        with(mViewModel) {
            listTopLevelCate.observe(this@HomeActivity, Observer {
                it?.let {
                    cate_group_view.setListCategory(it)
                    mMenuAdapter.setData(it)
                }
            })

            error.observe(this@HomeActivity,  Observer {
                this@HomeActivity.toast("Error ${it?.message}")
            })

            mViewModel.load()
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

        mMenuAdapter = HomeMenuItemAdapter()
        mMenuAdapter.setOnItemClickListener(object : HomeMenuItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                this@HomeActivity.onCategoryClick(position)
            }
        })
        recycler_view_home_menu.layoutManager = LinearLayoutManager(this)
        val space = getDimensionPixelSize(R.dimen.common_vert_medium_sub)
        recycler_view_home_menu.addItemDecoration(HomeMenuItemDecoration(space))
        recycler_view_home_menu.adapter = mMenuAdapter
    }

    private fun onCategoryClick(position: Int) {
        val category = mViewModel.getCategoryByIndex(position)
        category?.let {
            startActivity(PartnerListingActivity.getLaunchIntent(this@HomeActivity, category))
        }
    }

}
