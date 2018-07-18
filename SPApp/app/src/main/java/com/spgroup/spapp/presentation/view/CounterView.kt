package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_counter_view.view.*

class CounterView: LinearLayout {

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
        LayoutInflater.from(context).inflate(R.layout.layout_counter_view, this, true)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setEnablePlus(enable: Boolean) {
        iv_plus.isEnabled = enable
    }

    fun setEnableMinus(enable: Boolean) {
        iv_minus.isEnabled = enable
    }

    fun setCount(count: Int) {
        tv_count.setText("$count")
    }

}