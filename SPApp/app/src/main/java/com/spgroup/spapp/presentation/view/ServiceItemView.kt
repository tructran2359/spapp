package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.extension.getFormatedItemPrice
import kotlinx.android.synthetic.main.layout_service_item.view.*

class ServiceItemView : RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var serviceItem: ServiceItem

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context, serviceItem: ServiceItem) : super(context, null) {
        this.serviceItem = serviceItem
        init()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_service_item, this, true)
        tv_name.setText(serviceItem.name)
        tv_price.setText(serviceItem.price.getFormatedItemPrice(serviceItem.unit))
    }
}