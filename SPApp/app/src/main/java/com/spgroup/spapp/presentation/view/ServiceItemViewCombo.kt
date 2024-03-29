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
        private val initSelected: Boolean,
        private val itemListener: ServiceListingAdapter.OnItemInteractedListener
)
    : ServiceItemView(context) {

    init {
        inflate(R.layout.layout_service_item_combo, true)
        tv_name.text = service.label
        tv_price.isGone = service.priceText == null
        tv_price.text = service.priceText?.let { getPriceTextWithUnit(it, service.unit) }
        view_root.isSelected = initSelected
        iv_delete.isGone = !initSelected
        setOnClickListener { itemListener.onComplexCustomisationItemClick(service) }
        iv_delete.setOnClickListener {
            itemListener.onComplexCustomisationItemDelete(service)
            view_root.isSelected = false
            iv_delete.isGone = true
        }
    }

    private fun getPriceTextWithUnit(priceText: String, unit: String?): String {
        return if (unit != null && !unit.isEmpty()) {
            "$priceText / $unit"
        } else {
            priceText
        }
    }
}