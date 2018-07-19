package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.domain.model.ServiceItemCounter
import com.spgroup.spapp.presentation.adapter.ServiceGroupAdapter
import com.spgroup.spapp.util.extension.formatPriceWithUnit
import kotlinx.android.synthetic.main.layout_service_item_counter.view.*

class ServiceItemViewCounter: ServiceItemView {

    companion object {
        @JvmField val ACTION_PLUS = 1
        @JvmField val ACTION_DELETE = 2
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var serviceItem: ServiceItemCounter
    var itemPosition: Int
    var servicePosition: Int
    var listener: ServiceGroupAdapter.OnItemInteractedListener

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(
            context: Context,
            item: ServiceItem,
            servicePosition: Int,
            itemPosition: Int,
            listener: ServiceGroupAdapter.OnItemInteractedListener
    ): super(context, item) {

        serviceItem = item as ServiceItemCounter
        this.itemPosition = itemPosition
        this.servicePosition = servicePosition
        this.listener = listener
        init()

    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_service_item_counter, this, true)
        tv_name.setText(serviceItem.name)
        tv_price.setText(serviceItem.price.formatPriceWithUnit(serviceItem.unit))

        onCountUpdate()

        fl_add_btn_container.setOnClickListener {
            serviceItem.count++
            onCountUpdate()
        }

        iv_delete.setOnClickListener {
            serviceItem.count = 0
            onCountUpdate()
        }
    }

    fun onCountUpdate() {

        listener.onCountChanged(serviceItem.count, servicePosition, itemPosition)

        if (serviceItem.count == 0) {
            tv_count.visibility = View.GONE
            iv_delete.visibility = View.GONE
            iv_add.visibility = View.VISIBLE
            view_root.isSelected = false
        } else {
            tv_count.visibility = View.VISIBLE
            iv_delete.visibility = View.VISIBLE
            tv_count.setText(serviceItem.count.toString())
            iv_add.visibility = View.GONE
            view_root.isSelected = true
        }
    }
}