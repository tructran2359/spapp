package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceGroup
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.domain.model.ServiceItemCounter
import com.spgroup.spapp.presentation.view.ServiceItemViewCheckBox
import com.spgroup.spapp.presentation.view.ServiceItemViewCombo
import com.spgroup.spapp.presentation.view.ServiceItemViewCounter
import com.spgroup.spapp.util.doLogD
import kotlinx.android.synthetic.main.layout_service.view.*

class ServiceGroupAdapter(val mItemInteractedListener: OnItemInteractedListener): RecyclerView.Adapter<ServiceGroupAdapter.ServiceVH>() {

    companion object {
        val VIEW_TYPE_LAST = 1
        val VIEW_TYPE_NORMAL = 2
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var mData = mutableListOf<ServiceGroup>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_service, parent, false)
        with(view) {
            if (viewType == VIEW_TYPE_LAST) {
                v_devider_light.visibility = View.GONE
            } else {
                v_devider_light.visibility = View.VISIBLE
            }
        }
        return ServiceVH(view, mItemInteractedListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: ServiceVH, position: Int) {
        vh.bind(mData[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mData.size - 1) VIEW_TYPE_LAST else VIEW_TYPE_NORMAL
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun submitData(data: List<ServiceGroup>) {
        mData.clear()
        mData = data.toMutableList()
        notifyDataSetChanged()
    }

    fun collapseItem(position: Int) {
        val expanded = mData[position].expanded
        mData[position].expanded = expanded.not()
        notifyItemChanged(position)
    }

    fun getItem(servicePos: Int, itemPos: Int) = mData[servicePos].listItem[itemPos]

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnItemInteractedListener {
        fun onCollapseClick(positioni: Int)

        fun onServiceItemClick(servicePos: Int, itemPos: Int)

        fun onCountChanged(count: Int, servicePos: Int, itemPos: Int)

        fun onCheckChanged(checked: Boolean, servicePos: Int, itemPos: Int)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class ServiceVH(var view: View, var listener: OnItemInteractedListener) : RecyclerView.ViewHolder(view) {

        fun bind(service: ServiceGroup) {
            with(itemView) {
                tv_service_name.setText(service.name)
                if (service.description.isEmpty()) {
                    tv_service_description.visibility = View.GONE
                } else {
                    tv_service_description.visibility = View.VISIBLE
                    tv_service_description.setText(service.description)
                }

                if (service.expanded) {
                    doLogD("Test", "expanded")

                    ll_item_container.removeAllViews()
                    val itemCount = service.listItem.size
                    val servicePos = adapterPosition

                    for (i in 0 until itemCount) {
                        val item = service.listItem[i]
                        val view = when (item) {
                            is ServiceItemCounter -> {
                                ServiceItemViewCounter(itemView.context, item, servicePos, i, listener)
                            }

                            is ServiceItemCheckBox -> {
                                ServiceItemViewCheckBox(itemView.context, item, servicePos, i, listener)
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