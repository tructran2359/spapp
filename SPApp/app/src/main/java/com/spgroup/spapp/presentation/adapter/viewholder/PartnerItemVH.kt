package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.util.extension.loadImageWithPlaceholder
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.layout_partner_item.view.*

class PartnerItemVH(
        val view: View,
        private val itemClickListener: (View, PartnersListingItem, Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(partner: Partner) {
        view.run {
            tv_highlight.isGone = partner.highlight.isEmpty()
            tv_highlight.text = partner.highlight
            tv_name.text = partner.name
            tv_price.text = partner.priceDescription
            iv_logo.loadImageWithPlaceholder(
                    partner.imgUrl.toFullUrl(),
                    R.drawable.placeholder_icon,
                    R.drawable.placeholder_icon
            )

            setOnClickListener {
                itemClickListener.invoke(view, partner, adapterPosition)
            }
        }
    }
}