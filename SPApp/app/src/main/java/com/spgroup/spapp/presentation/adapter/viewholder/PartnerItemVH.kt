package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.util.extension.formatPriceWithUnit
import kotlinx.android.synthetic.main.layout_partner_item.view.*

class PartnerItemVH(
        val view: View,
        private val itemClickListener: (View, PartnersListingItem, Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(partner: Partner) {
        view.run {
            iv_sponsored.visibility = if (partner.isSponsored) View.VISIBLE else View.GONE
            tv_name.text = partner.name
            tv_price.text = partner.price.formatPriceWithUnit(partner.unit)
            setOnClickListener {
                itemClickListener.invoke(view, partner, adapterPosition)
            }
        }
    }
}