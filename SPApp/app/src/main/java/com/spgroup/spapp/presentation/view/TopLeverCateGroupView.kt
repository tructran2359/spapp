package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.view_top_level_category_group.view.*

class TopLeverCateGroupView: LinearLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mList: List<TopLevelCategory>
    private lateinit var mListCateView: List<TopLevelCateView>
    private var mListener: OnCategoryClickListener? = null

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
        LayoutInflater.from(context).inflate(R.layout.view_top_level_category_group, this, true)

        mListCateView = listOf(
                cate_view_0,
                cate_view_1,
                cate_view_2,
                cate_view_3,
                cate_view_4,
                cate_view_5)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setListCategory(list: List<TopLevelCategory>) {
        mList = list
        mList.forEachIndexed { index, cat ->
            if (index < mListCateView.size) {
                with(mListCateView[index]) {
                    setData(cat.name, cat.homeThumbnail.toFullUrl())
                    setOnClickListener {
                        mListener?.onCategoryClick(index)
                    }
                }
            }
        }
    }

    fun setOnCategoryClickListener(listener: OnCategoryClickListener) {
        mListener = listener
    }

    fun setIconSize(sizeInPixel: Int) {
        mListCateView.forEach { view ->
            view.setSizeInPixel(sizeInPixel)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnCategoryClickListener {
        fun onCategoryClick(position: Int)
    }

}