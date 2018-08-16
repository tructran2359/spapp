package com.spgroup.spapp.presentation.view

import android.content.Context
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_service_item_combo.view.*

class ServiceItemViewCombo(
        context: Context,
        service: ComplexCustomisationService)
    : ServiceItemView(context) {

    init {
        inflate(R.layout.layout_service_item_combo, true)
        tv_name.text = service.label
        tv_price.isGone = true
        tv_description.text = service.serviceDescription
    }
}