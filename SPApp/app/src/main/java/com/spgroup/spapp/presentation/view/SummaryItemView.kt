package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.formatPrice
import kotlinx.android.synthetic.main.layout_summary_item.view.*

class SummaryItemView: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mDeleteListener: (() -> Unit)? = null

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

        iv_delete.setOnClickListener {
            mDeleteListener?.let {
                it()
            }
        }
    }

    fun setName(name: String) {
        tv_name.setText(name)
    }

    fun setPrice(price: Float) {
        tv_est_price.text = price.formatPrice()
    }

    fun setLimit(min: Int, max: Int) {
        counter_view.setLimit(min, max)
    }

    fun setCount(count: Int) {
        counter_view.setCount(count)
    }

    fun getCount() = counter_view.getCount()

    fun initData(min: Int, max: Int, count: Int) {
        with(counter_view) {
            setLimit(min, max)
            setCount(count)
        }
    }

    fun setOnCountChangedListener(listener: CounterView.OnCountChangeListener?) {
        counter_view.setOnCountChangeListener(listener)
    }

    fun setOnDeleteListener(action: (() -> Unit)?) {
        mDeleteListener = action
    }

}