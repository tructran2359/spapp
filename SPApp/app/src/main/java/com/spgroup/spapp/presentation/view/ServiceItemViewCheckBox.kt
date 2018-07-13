package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CheckBoxServiceItem
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.extension.getFormatedItemPrice
import kotlinx.android.synthetic.main.layout_service_item_check_box.view.*

class ServiceItemViewCheckBox: ServiceItemView {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var serviceItem: CheckBoxServiceItem

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context, item: ServiceItem): super(context, item) {
        serviceItem = item as CheckBoxServiceItem
        init()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_service_item_check_box, this, true)
        tv_name.setText(serviceItem.name)
        tv_price.setText(serviceItem.price.getFormatedItemPrice(serviceItem.unit))
        tv_description.setText(serviceItem.description)

    }

}