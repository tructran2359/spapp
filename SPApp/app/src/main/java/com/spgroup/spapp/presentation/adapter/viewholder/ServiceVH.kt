package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.MultiplierService
import com.spgroup.spapp.domain.model.SubCategory
import com.spgroup.spapp.presentation.adapter.ServiceListingAdapter
import com.spgroup.spapp.presentation.view.ServiceItemViewCheckBox
import com.spgroup.spapp.presentation.view.ServiceItemViewCombo
import com.spgroup.spapp.presentation.view.ServiceItemViewCounter
import com.spgroup.spapp.util.doLogD
import kotlinx.android.synthetic.main.layout_service.view.*
import kotlinx.android.synthetic.main.layout_service_item_combo.view.*
import org.jetbrains.anko.collections.forEachWithIndex

class ServiceVH(
        val view: View,
        private val itemListener: ServiceListingAdapter.OnItemInteractedListener,
        private val onCollapseAction: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(subCat: SubCategory, mapSelectedValue: Map<Int, Int>, isExpanded: Boolean) {
        with(itemView) {
            tv_service_name.text = subCat.label
            if (subCat.description.isEmpty()) {
                tv_service_description.isGone = true
            } else {
                tv_service_description.isGone = false
                tv_service_description.text = subCat.description
            }

            if (isExpanded) {
                doLogD("Test", "expanded")

                ll_item_container.removeAllViews()

                subCat.services.forEachWithIndex { index, absServiceItem ->
                    val initCount = mapSelectedValue[absServiceItem.getServiceId()] ?: 0
                    val view = when (absServiceItem) {

                        is MultiplierService -> ServiceItemViewCounter(itemView.context, absServiceItem, initCount, itemListener)

                        is CheckboxService -> ServiceItemViewCheckBox(itemView.context, absServiceItem, initCount, itemListener)

                        is ComplexCustomisationService -> {
                            ServiceItemViewCombo(itemView.context, absServiceItem, itemListener).apply {
                                v_devider.isGone = (index == subCat.services.size - 1)
                            }
                        }
                    }

                    ll_item_container.addView(view)
                }

                iv_collapse.setImageResource(R.drawable.arrow_up_blue)
                ll_collapsed_container.visibility = View.VISIBLE

            } else {

                iv_collapse.setImageResource(R.drawable.arrow_down_blue)
                ll_collapsed_container.visibility = View.GONE

            }

            rl_service_info_container.setOnClickListener {
                onCollapseAction.invoke(adapterPosition)
            }
        }
    }
}