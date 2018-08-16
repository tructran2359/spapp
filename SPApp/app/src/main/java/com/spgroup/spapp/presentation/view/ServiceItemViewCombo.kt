package com.spgroup.spapp.presentation.view

import android.content.Context
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.presentation.adapter.ServiceListingAdapter
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_service_item_combo.view.*

class ServiceItemViewCombo(
        context: Context,
        private val service: ComplexCustomisationService,
        private val itemListener: ServiceListingAdapter.OnItemInteractedListener)
    : ServiceItemView(context) {

    init {
        inflate(R.layout.layout_service_item_combo, true)
        tv_name.text = service.label
        if (service.priceText == null) {
            tv_price.isGone = true
        } else {
            tv_price.isGone = false
            tv_price.text = service.priceText
        }
        setOnClickListener { itemListener.onComplexCustomisationItemClick(service) }
    }
}