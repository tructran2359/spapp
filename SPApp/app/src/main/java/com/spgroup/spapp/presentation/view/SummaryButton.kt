package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.extension.formatEstPrice
import com.spgroup.spapp.extension.formatPrice
import com.spgroup.spapp.extension.getColor
import kotlinx.android.synthetic.main.layout_summary_button.view.*

class SummaryButton: RelativeLayout {

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
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            rl_summary_button.setBackgroundResource(R.drawable.selector_btn_blue )
            tv_total_count.setTextColor(getColor(R.color.color_ui01))
        } else {
            rl_summary_button.setBackgroundResource(R.drawable.bg_rec_rounded_blue_disabled )
            tv_total_count.setTextColor(getColor(R.color.color_btninactive))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_summary_button, this, true)
    }

    fun setText(text: String) {
        tv_summary.setText(text)
    }

    fun setCount(count: Int) {
        tv_total_count.setText("$count")
    }

    fun setPrice(price: Float) {
        tv_price.setText(price.formatPrice())
    }

    fun setEstPrice(price: Float) {
        tv_price.setText(price.formatEstPrice())
    }

}