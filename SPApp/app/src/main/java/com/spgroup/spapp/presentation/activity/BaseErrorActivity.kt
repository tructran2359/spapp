package com.spgroup.spapp.presentation.activity

import android.os.Bundle
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDisplayMetrics
import kotlinx.android.synthetic.main.activity_error.*

open class BaseErrorActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val displayMetrics = getDisplayMetrics()
        val screenHeight = displayMetrics.heightPixels
        val iconMarginTop = screenHeight / 5
        val layoutParams = iv_error_icon.layoutParams as LinearLayout.LayoutParams
        layoutParams.topMargin = iconMarginTop
        iv_error_icon.layoutParams = layoutParams
    }
}