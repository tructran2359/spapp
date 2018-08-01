package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import kotlinx.android.synthetic.main.view_top_level_category_group.view.*

class TopLeverCateGroupView: LinearLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mList: List<TopLevelServiceCategory>
    private lateinit var mListCateView: List<TopLevelCateView>

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

    fun setListCategory(list: List<TopLevelServiceCategory>) {
        mList = list
        mList.forEachIndexed { index, topLevelServiceCategory ->
            val imageId = when (topLevelServiceCategory.id) {
                1 -> R.drawable.cate_food
                2 -> R.drawable.cate_house_keeping
                3 -> R.drawable.cate_aircon
                4 -> R.drawable.cate_laundry
                5 -> R.drawable.cate_edu
                6 -> R.drawable.cate_gro
                else -> throw IllegalArgumentException("ID ${topLevelServiceCategory.id} not found")
            }

            mListCateView[index].setData(topLevelServiceCategory.name, imageId)
        }
    }
}