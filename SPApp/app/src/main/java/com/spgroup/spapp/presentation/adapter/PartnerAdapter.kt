package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion
import com.spgroup.spapp.presentation.adapter.viewholder.PartnerItemVH
import com.spgroup.spapp.presentation.adapter.viewholder.PromotionItemVH
import com.spgroup.spapp.util.extension.inflate

class PartnerAdapter(
        private val onItemClickListener: (View, PartnersListingItem, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        const val UNEXPECTED = -1
        const val TYPE_PARTNER = 1
        const val TYPE_PROMOTION = 2
    }

    private val mData = mutableListOf<PartnersListingItem>()

    ///////////////////////////////////////////////////////////////////////////
    // Overrie
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PARTNER -> {
                val view = parent.inflate(R.layout.layout_partner_item, false)
                PartnerItemVH(view, onItemClickListener)
            }
            TYPE_PROMOTION -> {
                val view = parent.inflate(R.layout.layout_partner_promotion, false)
                PromotionItemVH(view, onItemClickListener)
            }
            else -> throw IllegalArgumentException("Undefined type")
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_PARTNER -> (vh as PartnerItemVH).bind(mData[position] as Partner)
            // To Simulate empty price desc
//            TYPE_PARTNER -> (vh as PartnerItemVH).bind((mData[position] as Partner).copy(priceDescription = ""))
            TYPE_PROMOTION -> (vh as PromotionItemVH).bind(mData[position] as Promotion)
            else -> throw IllegalArgumentException("Undefined type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mData[position]) {
            is Partner -> TYPE_PARTNER
            is Promotion -> TYPE_PROMOTION
            else -> UNEXPECTED
        }
    }

    override fun getItemCount() = mData.size

///////////////////////////////////////////////////////////////////////////
// Other
///////////////////////////////////////////////////////////////////////////

    fun setData(list: List<PartnersListingItem>?) {
        list?.let {
            mData.clear()
            mData.addAll(list)
            notifyDataSetChanged()
        }
    }
}