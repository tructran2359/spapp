package com.spgroup.spapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.spgroup.spapp.R

class CustomiseDropdownAdapter(
        val mContext: Context,
        val layoutId: Int,
        val data: MutableList<String>
) : ArrayAdapter<String> (mContext, layoutId, data) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(mContext).inflate(R.layout.layout_preferred_time_dropdown, parent, false)

        (view as TextView).setText(data[position])
        return view
    }

    fun setData(list: List<String>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

}