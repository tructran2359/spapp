package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.presentation.adapter.ServiceGroupAdapter
import com.spgroup.spapp.util.extension.formatPriceWithUnit
import kotlinx.android.synthetic.main.layout_service_item_check_box.view.*

class ServiceItemViewCheckBox: ServiceItemView {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var serviceItem: ServiceItemCheckBox
    var servicePos: Int
    var itemPos: Int
    var listener: ServiceGroupAdapter.OnItemInteractedListener

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context, item: ServiceItem, servicePos: Int, itemPos: Int, listener: ServiceGroupAdapter.OnItemInteractedListener): super(context, item) {
        serviceItem = item as ServiceItemCheckBox
        this.servicePos = servicePos
        this.itemPos = itemPos
        this.listener = listener
        init()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_service_item_check_box, this, true)
        tv_name.setText(serviceItem.name)
        tv_price.setText(serviceItem.price.formatPriceWithUnit(serviceItem.unit))
        tv_description.setText(serviceItem.description)
        onCheckUpdated()

        fl_check_container.setOnClickListener {
            serviceItem.selected = !serviceItem.selected
            onCheckUpdated()
        }
    }

    fun onCheckUpdated() {
        view_root.isSelected = serviceItem.selected
        listener.onCheckChanged(serviceItem.selected, servicePos, itemPos)
    }

}