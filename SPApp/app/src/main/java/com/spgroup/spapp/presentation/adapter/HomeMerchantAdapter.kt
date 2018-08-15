package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.presentation.adapter.viewholder.HomeMerchantVH
import com.spgroup.spapp.util.extension.inflate

class HomeMerchantAdapter(private var mListener: OnMerchantClickListener): RecyclerView.Adapter<HomeMerchantVH>() {


    private var mData = mutableListOf<TopLevelFeaturedPartner>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMerchantVH {
        val view = parent.inflate(R.layout.layout_merchant, false)
        return HomeMerchantVH(view, mListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: HomeMerchantVH, position: Int) {
        vh.bind(mData[position])
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setOnMerchantClickListener(listener: OnMerchantClickListener) {
        mListener = listener
    }

    fun setData(data: List<TopLevelFeaturedPartner>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnMerchantClickListener {
        fun onMerchantClick(position: Int)
    }
}