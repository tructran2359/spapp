package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelCategory
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
        mList.forEachIndexed { index, topLevelServiceCategory ->
            val imageId = when (topLevelServiceCategory.id) {
                "food" -> R.drawable.cate_food
                "housekeeping" -> R.drawable.cate_house_keeping
                "aircon" -> R.drawable.cate_aircon
                "laundry" -> R.drawable.cate_laundry
                "education" -> R.drawable.cate_edu
                "groceries" -> R.drawable.cate_gro
                else -> throw IllegalArgumentException("ID ${topLevelServiceCategory.id} not found")
            }

            with(mListCateView[index]) {
                setData(topLevelServiceCategory.name, imageId)
                setOnClickListener {
                    mListener?.onCategoryClick(index)
                }
            }
        }
    }

    fun setOnCategoryClickListener(listener: OnCategoryClickListener) {
        mListener = listener
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnCategoryClickListener {
        fun onCategoryClick(position: Int)
    }

}