package com.spgroup.spapp.presentation.view

import android.content.Context
import android.text.Spannable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.getDrawable
import kotlinx.android.synthetic.main.layout_menu_text_view.view.*


class MenuTextView: LinearLayout {

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
        LayoutInflater.from(context).inflate(R.layout.layout_menu_text_view, this, true)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setText(text: String) {
        val spannableFactory = Spannable.Factory.getInstance()
        val spannable = spannableFactory.newSpannable(text + "   ")
        val drawable = getDrawable(R.drawable.pdf)
        val iconSizeWidth = getDimensionPixelSize(R.dimen.pdf_width)
        val iconSizeHeight = getDimensionPixelSize(R.dimen.pdf_height)
        drawable?.let {
            it.setBounds(0, 0, iconSizeWidth, iconSizeHeight)

            val imageSpan = CenterImageSpan(it)
            val length = spannable.length
            spannable.setSpan(imageSpan, length - 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            tv_link.setText(spannable)
        }
    }
}