package com.spgroup.spapp.presentation.view

import android.content.Context
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_service_item_check_box.view.*

class ServiceItemViewCheckBox(
        context: Context,
        service: CheckboxService
) : ServiceItemView(context) {

    private var isChecked = false

    init {
        inflate(R.layout.layout_service_item_check_box, true)
        tv_name.text = service.label
        tv_price.text = service.priceText
        tv_description.text = service.serviceDescription
        onCheckUpdated()

        fl_check_container.setOnClickListener {
            isChecked = isChecked.not()
            onCheckUpdated()
        }
    }

    private fun onCheckUpdated() {
        view_root.isSelected = isChecked
    }

}