package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.util.extension.toFullImgUrl
import kotlinx.android.synthetic.main.layout_partner_item.view.*

class PartnerItemVH(
        val view: View,
        private val itemClickListener: (View, PartnersListingItem, Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(partner: Partner) {
        view.run {
            iv_sponsored.visibility = if (partner.highlight.isEmpty()) View.GONE else View.VISIBLE
            tv_name.text = partner.name
            tv_price.text = partner.priceDescription
            Glide.with(context)
                    .load(partner.imgUrl.toFullImgUrl())
                    .into(iv_logo)

            setOnClickListener {
                itemClickListener.invoke(view, partner, adapterPosition)
            }
        }
    }
}