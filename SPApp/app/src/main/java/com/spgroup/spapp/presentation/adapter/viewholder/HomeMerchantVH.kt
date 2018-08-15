package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.presentation.adapter.HomeMerchantAdapter
import com.spgroup.spapp.util.extension.loadImage
import com.spgroup.spapp.util.extension.toFullImgUrl
import kotlinx.android.synthetic.main.layout_merchant.view.*

class HomeMerchantVH(itemView: View, private val listener: HomeMerchantAdapter.OnMerchantClickListener): RecyclerView.ViewHolder(itemView) {

    fun bind(partner: TopLevelFeaturedPartner) {
        itemView.run {
            tv_category.text = partner.category
            tv_partner_name.text = partner.name
            iv_logo.loadImage(partner.logoUrl.toFullImgUrl())
            setOnClickListener {
                listener.onMerchantClick(partner, adapterPosition)
            }
        }
    }
}