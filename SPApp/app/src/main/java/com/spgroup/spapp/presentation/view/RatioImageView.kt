package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import com.makeramen.roundedimageview.RoundedImageView

class RatioImageView: RoundedImageView {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mRatioWidth = 0
    private var mRatioHeight = 0
    private var mDependOnWidth = true

    ///////////////////////////////////////////////////////////////////////////
    // Constructor - Override
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
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mRatioHeight == 0 || mRatioWidth == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            var width = 0
            var height = 0
            if (mDependOnWidth) {
                width = widthMeasureSpec
                height = mRatioHeight * widthMeasureSpec / mRatioWidth
            } else {
                height = heightMeasureSpec
                width = mRatioWidth * heightMeasureSpec / mRatioHeight
            }
            super.onMeasure(width, height)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setRatio(width: Int, height: Int, dependOnWidth: Boolean) {
        mDependOnWidth = dependOnWidth
        mRatioWidth = width
        mRatioHeight = height
        invalidate()
    }
}