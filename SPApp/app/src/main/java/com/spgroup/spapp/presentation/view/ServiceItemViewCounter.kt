package com.spgroup.spapp.presentation.view

import android.content.Context
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.MultiplierService
import com.spgroup.spapp.presentation.adapter.ServiceListingAdapter
import com.spgroup.spapp.util.extension.formatPriceWithUnit
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_service_item_counter.view.*

class ServiceItemViewCounter(
        context: Context,
        private val service: MultiplierService,
        private var count: Int,
        private val itemListener: ServiceListingAdapter.OnItemInteractedListener
) : ServiceItemView(context) {

    init {
        inflate(R.layout.layout_service_item_counter, true)
        tv_name.text = service.label
        tv_price.text = service.price.formatPriceWithUnit(service.unit)
        onCountUpdate()
        setOnClickListener {
            if (count < service.max) {
                count++
                if (count < service.min) {
                    count = service.min
                }
                onCountUpdate()
            }
        }

        iv_delete.setOnClickListener {
            count = 0
            onCountUpdate()
        }
    }

    private fun onCountUpdate() {
        itemListener.onMultiplierItemChanged(service, count)
        if (count == 0) {
            tv_count.visibility = View.GONE
            iv_delete.visibility = View.GONE
            iv_add.visibility = View.VISIBLE
            view_root.isSelected = false
        } else {
            tv_count.visibility = View.VISIBLE
            iv_delete.visibility = View.VISIBLE
            tv_count.text = count.toString()
            iv_add.visibility = View.GONE
            view_root.isSelected = true
        }
    }
}