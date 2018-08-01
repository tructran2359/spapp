package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_merchant.view.*

class HomeMerchantAdapter: RecyclerView.Adapter<HomeMerchantAdapter.HomeMerchantVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMerchantVH {
        val view = parent.inflate(R.layout.layout_merchant, false)
        return HomeMerchantVH(view)
    }

    override fun getItemCount() = 30

    override fun onBindViewHolder(vh: HomeMerchantVH, position: Int) {
        vh.bind(position)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class HomeMerchantVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            with(itemView) {
                tv_merchant_name.setText("Cate $position")
            }
        }
    }
}