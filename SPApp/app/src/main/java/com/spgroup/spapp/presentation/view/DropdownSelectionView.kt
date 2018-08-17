package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.PreferredTimeAdapter
import kotlinx.android.synthetic.main.view_dropdown_selection.view.*

class DropdownSelectionView: LinearLayout {

    private lateinit var mAdapter: PreferredTimeAdapter

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
        LayoutInflater.from(context).inflate(R.layout.view_dropdown_selection, this, true)
        mAdapter = PreferredTimeAdapter(context, R.layout.layout_preferred_time, mutableListOf())
        spinner_options.adapter = mAdapter
    }

    fun setTitle(title: String) {
        tv_name.text = title
    }

    fun setData(list: List<String>) {
        mAdapter.setData(list)
    }
}