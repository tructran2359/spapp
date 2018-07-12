package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.extension.getFormatedItemPrice
import kotlinx.android.synthetic.main.layout_service_item.view.*

class ServiceItemAdapter: RecyclerView.Adapter<ServiceItemAdapter.ServiceItemVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mData = mutableListOf<ServiceItem>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_service_item, parent, false)
        return ServiceItemVH(view)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: ServiceItemVH, position: Int) {
        vh.bind(mData[position])
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun submitData(data: List<ServiceItem>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class ServiceItemVH(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: ServiceItem) {
            with(itemView) {
                tv_name.setText(item.name)
                tv_price.setText(item.price.getFormatedItemPrice(item.unit))
            }
        }
    }
}