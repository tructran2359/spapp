package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.formatPrice
import com.spgroup.spapp.util.extension.getColorFromRes
import com.spgroup.spapp.util.extension.toDiscountText
import kotlinx.android.synthetic.main.view_price_detail.view.*

class PriceDetailView: RelativeLayout {

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
        LayoutInflater.from(context).inflate(R.layout.view_price_detail, this, true)
    }

    fun setLabel(label: String) {
        tv_discount.text = label
    }

    fun setPrice(price: Float, isDiscount: Boolean) {
        if (isDiscount) {
            tv_discount_value.text = price.toDiscountText()
            tv_discount_value.setTextColor(context.getColorFromRes(R.color.color_alert))
        } else {
            tv_discount_value.text = price.formatPrice()
            tv_discount_value.setTextColor(context.getColorFromRes(R.color.color_black))
        }
    }
}