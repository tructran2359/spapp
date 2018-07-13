package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CategoryService
import com.spgroup.spapp.presentation.view.ServiceItemViewCounter
import kotlinx.android.synthetic.main.layout_service.view.*

class CategoryServiceAdapter: RecyclerView.Adapter<CategoryServiceAdapter.ServiceVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mData = mutableListOf<CategoryService>()
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

    fun submitData(data: List<CategoryService>) {
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

        fun bind(service: CategoryService) {
            with(itemView) {
                tv_service_name.setText(service.name)

                if (service.expanded) {

                    ll_item_container.removeAllViews()
                    val itemCount = service.listItem.size
                    for (i in 0 until itemCount) {
                        ll_item_container.addView(ServiceItemViewCounter(itemView.context, service.listItem[i]))
                    }

                    iv_collapse.setImageResource(R.drawable.arrow_up_blue)
                    ll_item_container.visibility = View.VISIBLE

                } else {

                    iv_collapse.setImageResource(R.drawable.arrow_down_blue)
                    ll_item_container.visibility = View.GONE

                }

                // Wait to other story to add functionality for button
//                rl_service_info_container.setOnClickListener {
//                    listener.onCollapseClick(adapterPosition)
//                }
            }
        }
    }
}