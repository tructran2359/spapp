package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_summary_item.view.*

class SummaryItemView: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context): super(context){
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle) {
        initViews(context)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_summary_item, this, true)
    }

    fun setName(name: String) {
        tv_name.setText(name)
    }

    fun setLimit(min: Int, max: Int) {
        counter_view.setLimit(min, max)
    }

    fun setCount(count: Int) {
        counter_view.setCount(count)
    }

    fun initData(min: Int, max: Int, count: Int) {
        with(counter_view) {
            setLimit(min, max)
            setCount(count)
        }
    }

}