package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_customise_counter.view.*

class CustomiseCounterView: RelativeLayout {

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

    fun enablePlus(enable: Boolean) {
        counter_view.setEnablePlus(enable)
    }

    fun enableMinus(enable: Boolean) {
        counter_view.setEnableMinus(enable)
    }

}