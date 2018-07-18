package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_customise_counter.view.*

class CustomiseCounterView: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var mCount = 0
    var mMin = 0
    var mMax = 0

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

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_customise_counter, this, true)
    }

    fun setName(name: String) {
        tv_name.setText(name)
    }

    fun setOption(option: String) {
        tv_option.setText(option)
    }

    fun setLimit(min: Int, max: Int) {
        counter_view.setLimit(min, max)
    }

    fun setCount(count: Int) {
        counter_view.setCount(count)
    }

    fun enablePlus(enable: Boolean) {
        counter_view.setEnablePlus(enable)
    }

    fun enableMinus(enable: Boolean) {
        counter_view.setEnableMinus(enable)
    }

}