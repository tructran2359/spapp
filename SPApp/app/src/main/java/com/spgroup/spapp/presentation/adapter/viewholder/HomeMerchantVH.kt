package com.spgroup.spapp.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.presentation.adapter.HomeMerchantAdapter
import com.spgroup.spapp.util.extension.loadImageWithPlaceholder
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.layout_merchant.view.*

class HomeMerchantVH(itemView: View, private val listener: HomeMerchantAdapter.OnMerchantClickListener): RecyclerView.ViewHolder(itemView) {

    fun bind(partner: TopLevelFeaturedPartner) {
        itemView.run {
            tv_category.text = partner.category
            tv_partner_name.text = partner.name
            iv_logo.loadImageWithPlaceholder(
                    partner.logoUrl.toFullUrl(),
                    R.drawable.placeholder_icon,
                    R.drawable.placeholder_icon
            )
            setOnClickListener {
                listener.onMerchantClick(partner, adapterPosition)
            }
        }
    }
}