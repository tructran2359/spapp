package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_indicator_text_view.view.*

class IndicatorTextView(context: Context, text: String): LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_indicator_text_view, this, true)
        tv_content.setText(text)
    }
}