package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.loadImageWithPlaceholder
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.layout_partner_promotion.view.*

class PromotionItemVH(
        val view: View,
        private val itemClickListener: (View, PartnersListingItem, Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(promotion: Promotion) {
        view.run {
            iv_promotion.loadImageWithPlaceholder(
                    promotion.imagePath.toFullUrl(),
                    R.drawable.placeholder_icon,
                    R.drawable.placeholder_icon
            )
            tv_partner_name.text = promotion.partnerName

            tv_partner_name.post {
                val count = tv_partner_name.lineCount
                doLogD("LineCount", "Text: ${promotion.partnerName} count: $count")
                if (count == 1) {
                    tv_promotion.maxLines = 3
                } else {
                    tv_promotion.maxLines = 2
                }
            }

            tv_promotion.text = promotion.promoText
            setOnClickListener {
                itemClickListener.invoke(view, promotion, adapterPosition)
            }
        }
    }
}