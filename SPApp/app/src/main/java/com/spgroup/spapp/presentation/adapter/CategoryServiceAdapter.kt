package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CheckBoxServiceItem
import com.spgroup.spapp.domain.model.CounterServiceItem
import com.spgroup.spapp.domain.model.ServiceGroup
import com.spgroup.spapp.presentation.view.ServiceItemViewCheckBox
import com.spgroup.spapp.presentation.view.ServiceItemViewCounter
import kotlinx.android.synthetic.main.layout_service.view.*

class CategoryServiceAdapter: RecyclerView.Adapter<CategoryServiceAdapter.ServiceVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mData = mutableListOf<ServiceGroup>()
    val mItemInteractedListener = object : OnItemInteractedListener {
        override fun onCollapseClick(position: Int) {
            val expanded = mData[position].expanded
            mData[position].expanded = expanded.not()
            notifyItemChanged(position)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_service, parent, false);
        return ServiceVH(view, mItemInteractedListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: ServiceVH, positioni: Int) {
        vh.bind(mData[positioni])
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun submitData(data: List<ServiceGroup>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnItemInteractedListener {
        fun onCollapseClick(positioni: Int)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class ServiceVH(var view: View, var listener: OnItemInteractedListener) : RecyclerView.ViewHolder(view) {

        fun bind(service: ServiceGroup) {
            with(itemView) {
                tv_service_name.setText(service.name)
                tv_service_description.setText(service.description)

                if (service.expanded) {

                    ll_item_container.removeAllViews()
                    val itemCount = service.listItem.size
                    for (i in 0 until itemCount) {
                        val item = service.listItem[i]
                        when (item) {
                            is CounterServiceItem -> {
                                ll_item_container.addView(ServiceItemViewCounter(itemView.context, item))
                            }

                            is CheckBoxServiceItem -> {
                                ll_item_container.addView(ServiceItemViewCheckBox(itemView.context, item))
                            }
                        }

                    }

                    iv_collapse.setImageResource(R.drawable.arrow_up_blue)
                    ll_collapsed_container.visibility = View.VISIBLE

                } else {

                    iv_collapse.setImageResource(R.drawable.arrow_down_blue)
                    ll_collapsed_container.visibility = View.GONE

                }

                rl_service_info_container.setOnClickListener {
                    listener.onCollapseClick(adapterPosition)
                }
            }
        }
    }
}