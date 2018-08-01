package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.HomePromotionAdapter
import com.spgroup.spapp.presentation.viewmodel.HomeViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.getDisplayMetrics
import com.spgroup.spapp.util.extension.toast
import kotlinx.android.synthetic.main.activity_home.*

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
    }

    fun subcribeUI() {
        val factory = ViewModelFactory.getInstance()
        mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        with(mViewModel) {
            listTopLevelCate.observe(this@HomeActivity, Observer {
                it?.let {
                    cate_group_view.setListCategory(it)
                }
            })

            error.observe(this@HomeActivity,  Observer {
                this@HomeActivity.toast("Error ${it?.message}")
            })

            mViewModel.load()
        }
    }

    fun setUpMenu() {
        mMenu = SlidingMenu(this)

        with(mMenu) {
            touchModeAbove = SlidingMenu.TOUCHMODE_NONE
            setShadowWidthRes(R.dimen.common_spacing)
            setShadowDrawable(R.drawable.gradient_menu_shadow)
            setBehindOffsetRes(R.dimen.menu_margin_right)
            setMenu(R.layout.menu_home)
            attachToActivity(this@HomeActivity, SlidingMenu.SLIDING_CONTENT)
        }

        val promotionLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_promotions.layoutManager = promotionLayoutManager

        val displayMetrics = getDisplayMetrics()
        val screenWidth = displayMetrics.widthPixels
        recycler_view_promotions.adapter = HomePromotionAdapter(this, screenWidth)
    }

}
