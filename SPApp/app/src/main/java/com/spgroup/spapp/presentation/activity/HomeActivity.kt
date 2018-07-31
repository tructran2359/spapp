package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu
import com.spgroup.spapp.R

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

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setupViews() {
        setUpMenu()

    }

    fun setUpMenu() {
        mMenu = SlidingMenu(this)
        with(mMenu) {
            touchModeAbove = SlidingMenu.TOUCHMODE_FULLSCREEN
            setShadowWidthRes(R.dimen.common_spacing)
            setShadowDrawable(R.drawable.gradient_menu_shadow)
            setBehindOffsetRes(R.dimen.menu_margin_right)
            setMenu(R.layout.menu_home)
            attachToActivity(this@HomeActivity, SlidingMenu.SLIDING_CONTENT)
        }
    }

}
