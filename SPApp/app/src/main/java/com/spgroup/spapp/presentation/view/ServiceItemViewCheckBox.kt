package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.extension.formatPriceWithUnit
import kotlinx.android.synthetic.main.layout_service_item_check_box.view.*

class ServiceItemViewCheckBox: ServiceItemView {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var serviceItem: ServiceItemCheckBox

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context, item: ServiceItem): super(context, item) {
        serviceItem = item as ServiceItemCheckBox
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
    }

}