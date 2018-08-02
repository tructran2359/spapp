package com.spgroup.spapp.presentation.view.behavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDimensionPixelSize

class HomeMenuViewBehavior(
        val context: Context,
        attrs: AttributeSet?
) : CoordinatorLayout.Behavior<ImageView>(context, attrs) {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mMaxScroll: Int = 0
    private var mIsBlue = true

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        val appBarLayout = dependency as AppBarLayout
        initProperties(appBarLayout)

        val currentScroll = dependency.bottom
        val actionBarHeight = context.getDimensionPixelSize(R.dimen.action_bar_height)
        if (currentScroll < actionBarHeight && mIsBlue) {
            child.setImageResource(R.drawable.ic_menu_white)
            mIsBlue = false
        } else if (currentScroll >= actionBarHeight && !mIsBlue) {
            child.setImageResource(R.drawable.ic_menu_blue)
            mIsBlue = true
        }
        return true
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initProperties(appBarLayout: AppBarLayout) {
        mMaxScroll = appBarLayout.totalScrollRange
    }
}