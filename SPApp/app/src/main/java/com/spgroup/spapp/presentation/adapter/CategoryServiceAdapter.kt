package com.spgroup.spapp.presentation.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceGroup
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.domain.model.ServiceItemCounter
import com.spgroup.spapp.presentation.activity.CustomiseActivity
import com.spgroup.spapp.presentation.view.ServiceItemViewCheckBox
import com.spgroup.spapp.presentation.view.ServiceItemViewCombo
import com.spgroup.spapp.presentation.view.ServiceItemViewCounter
import kotlinx.android.synthetic.main.layout_service.view.*

class CategoryServiceAdapter(val context: Context): RecyclerView.Adapter<CategoryServiceAdapter.ServiceVH>() {

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

        override fun onServiceItemClick(servicePos: Int, itemPos: Int) {
            val serviceItem = mData[servicePos].listItem[itemPos]
            if (serviceItem is ServiceItemCombo) {
                val intent = CustomiseActivity.getLaunchIntent(context, serviceItem)
                context.startActivity(intent)
            }
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

        fun onServiceItemClick(servicePos: Int, itemPos: Int)
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
                    val servicePos = adapterPosition

                    for (i in 0 until itemCount) {
                        val item = service.listItem[i]
                        val view = when (item) {
                            is ServiceItemCounter -> {
                                ServiceItemViewCounter(itemView.context, item)
                            }

                            is ServiceItemCheckBox -> {
                                ServiceItemViewCheckBox(itemView.context, item)
                            }

                            is ServiceItemCombo -> {
                                ServiceItemViewCombo(itemView.context, item)
                            }

                            else -> null
                        }

                        view?.let {
                            view.setOnClickListener {
                                listener.onServiceItemClick(servicePos, i)
                            }
                            ll_item_container.addView(view)
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