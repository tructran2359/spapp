package com.spgroup.spapp.presentation.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Supplier
import com.spgroup.spapp.presentation.adapter.diff_utils.PartnerListingDiffCallback
import com.spgroup.spapp.util.extension.formatPriceWithUnit
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_partner_item.view.*
import kotlinx.android.synthetic.main.layout_partner_promotion.view.*

class PartnerAdapter: RecyclerView.Adapter<PartnerAdapter.PartnerVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val TYPE_PARTNER = 1
    val TYPE_PROMOTION = 2

    var mData = mutableListOf<Supplier>()

    ///////////////////////////////////////////////////////////////////////////
    // Overrie
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerVH {
        val view = when(viewType) {

            TYPE_PARTNER -> parent.inflate(R.layout.layout_partner_item, false)

            TYPE_PROMOTION -> parent.inflate(R.layout.layout_partner_promotion, false)

            else -> throw IllegalArgumentException("Undefined type")
        }

        return PartnerVH(view)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: PartnerVH, position: Int) {
        vh.bind(mData[position])
    }

    override fun getItemViewType(position: Int) = if (mData[position].isPromotion) TYPE_PROMOTION else TYPE_PARTNER

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setData(list: List<Supplier>?) {
        list?.let {
            val diffCallback = PartnerListingDiffCallback(mData, list)
            val result = DiffUtil.calculateDiff(diffCallback)
            mData.clear()
            mData.addAll(list)
            result.dispatchUpdatesTo(this)

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class PartnerVH(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(partner: Supplier) {
            if (partner.isPromotion) {
                bindPromotion(partner)
            } else {
                bindPartner(partner)
            }
        }

        fun bindPartner(partner: Supplier) {
            view.run {
                iv_sponsored.visibility = if (partner.isSponsored) View.VISIBLE else View.GONE
                tv_name.setText(partner.name)
                tv_price.setText(partner.price.formatPriceWithUnit(partner.unit))
            }
        }

        fun bindPromotion(partner: Supplier) {
            view.run {
                iv_promotion.setImageResource(R.drawable.rice_temp)
                tv_promotion.setText(partner.name)
            }
        }
    }
}