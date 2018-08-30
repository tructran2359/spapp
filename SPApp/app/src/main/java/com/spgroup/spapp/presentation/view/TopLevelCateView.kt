package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.loadImage
import kotlinx.android.synthetic.main.view_top_level_category.view.*

class TopLevelCateView: LinearLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle) {
        initViews(context)
    }

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_top_level_category, this, true)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Set data to show  return
     *
     * @param cateName : category name
     * @param cateImage: drawable id for dummy data
     */
    fun setData(cateName: String, cateImage: String) {
        tv_cate_name.setText(cateName)
        iv_cate_image.loadImage(cateImage)
    }
}