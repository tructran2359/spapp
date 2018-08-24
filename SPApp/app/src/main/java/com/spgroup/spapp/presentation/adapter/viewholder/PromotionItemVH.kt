package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.layout_partner_promotion.view.*

class PromotionItemVH(
        val view: View,
        private val itemClickListener: (View, PartnersListingItem, Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(promotion: Promotion) {
        view.run {
            Glide.with(context)
                    .load(promotion.imagePath.toFullUrl())
                    .into(iv_promotion)
            tv_partner_name.text = promotion.partnerName
            tv_promotion.text = promotion.promoText
            setOnClickListener {
                itemClickListener.invoke(view, promotion, adapterPosition)
            }
        }
    }
}