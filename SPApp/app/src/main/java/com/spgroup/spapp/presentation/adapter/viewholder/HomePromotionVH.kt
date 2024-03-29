package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.spgroup.spapp.domain.model.TopLevelPromotion
import com.spgroup.spapp.presentation.adapter.HomePromotionAdapter
import com.spgroup.spapp.util.extension.loadImageWithDefaultPlaceholder
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.layout_top_level_promotion.view.*

class HomePromotionVH(
        itemView: View,
        private val listener: HomePromotionAdapter.OnPromotionClickListener): RecyclerView.ViewHolder(itemView) {

    fun bind(promotion: TopLevelPromotion) {
        itemView.run {
            tv_name.text = promotion.partnerName
            tv_description.text = promotion.promoText
            iv_image.loadImageWithDefaultPlaceholder(promotion.imageUrl.toFullUrl())
            setOnClickListener {
                listener.onPromotionClick(promotion, adapterPosition)
            }
        }
    }
}