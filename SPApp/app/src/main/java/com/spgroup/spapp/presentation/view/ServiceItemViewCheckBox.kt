package com.spgroup.spapp.presentation.view

import android.content.Context
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.presentation.adapter.ServiceListingAdapter
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_service_item_check_box.view.*

class ServiceItemViewCheckBox(
        context: Context,
        private val service: CheckboxService,
        count: Int,
        private val itemListener: ServiceListingAdapter.OnItemInteractedListener
) : ServiceItemView(context) {

    private var isChecked: Boolean

    init {
        inflate(R.layout.layout_service_item_check_box, true)
        tv_name.text = service.label
        tv_price.text = service.priceText
        tv_description.text = service.serviceDescription
        isChecked = (count == 1)
        onCheckUpdated()

        setOnClickListener {
            isChecked = isChecked.not()
            onCheckUpdated()
        }
    }

    private fun onCheckUpdated() {
        itemListener.onCheckboxItemChanged(service, isChecked)
        view_root.isSelected = isChecked
    }

}