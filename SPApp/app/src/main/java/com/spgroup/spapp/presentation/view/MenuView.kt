package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Pdf
import kotlinx.android.synthetic.main.layout_menu_view.view.*

class MenuView: LinearLayout {

    var mListener: OnMenuItemClickListener? = null

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
        LayoutInflater.from(context).inflate(R.layout.layout_menu_view, this, true)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun addPdf(pdf: Pdf) {
        val view = MenuTextView(context)
        view.setText(pdf.title)
        view.setOnClickListener {
            mListener?.onMenuItemClick(pdf.title, pdf.uri)
        }
        ll_menu_container.addView(view)
    }

    fun setMenuName(menuName: String) {
        tv_name.setText(menuName)
    }

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        mListener = listener
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnMenuItemClickListener {
        fun onMenuItemClick(title: String, uri: String)
    }
}