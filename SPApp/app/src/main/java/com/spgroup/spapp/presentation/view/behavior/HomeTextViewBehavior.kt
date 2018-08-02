package com.spgroup.spapp.presentation.view.behavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDimensionPixelSize

class HomeTextViewBehavior(
        val context: Context,
        attrs: AttributeSet?
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mMaxScroll: Int = 0

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val appBarLayout = dependency as AppBarLayout
        initProperties(appBarLayout)

        val currentScroll = dependency.bottom
        val actionBarHeight = context.getDimensionPixelSize(R.dimen.action_bar_height)
        if (currentScroll <= actionBarHeight) {
            child.alpha = 1f
        } else {
            val percentage = (currentScroll.toFloat()) / (mMaxScroll.toFloat() - actionBarHeight)
            child.alpha = 1f - percentage
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