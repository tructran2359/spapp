package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.CustomiseDropdownAdapter
import kotlinx.android.synthetic.main.view_dropdown_selection.view.*

class DropdownSelectionView: LinearLayout {

    private lateinit var mAdapter: CustomiseDropdownAdapter
    private var listener: ((Int) -> Unit)? = null

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
        mAdapter = CustomiseDropdownAdapter(context, R.layout.layout_preferred_time, mutableListOf())
        spinner_options.adapter = mAdapter
        spinner_options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listener?.invoke(position)
            }

        }
    }

    fun setTitle(title: String) {
        tv_name.text = title
    }

    fun setData(list: List<String>) {
        mAdapter.setData(list)
    }

    fun getSelectedPosition() = spinner_options.selectedItemPosition

    fun setOnItemSelectedListener(action: (Int) -> Unit) {
        listener = action
    }
}