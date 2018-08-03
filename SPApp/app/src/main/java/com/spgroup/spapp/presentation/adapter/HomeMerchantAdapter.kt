package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_merchant.view.*

class HomeMerchantAdapter: RecyclerView.Adapter<HomeMerchantAdapter.HomeMerchantVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mListener: OnMerchantClickListener? = null

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMerchantVH {
        val view = parent.inflate(R.layout.layout_merchant, false)
        return HomeMerchantVH(view)
    }

    override fun getItemCount() = 30

    override fun onBindViewHolder(vh: HomeMerchantVH, position: Int) {
        vh.bind(position)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setOnMerchantClickListener(listener: OnMerchantClickListener) {
        mListener = listener
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    inner class HomeMerchantVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                mListener?.onMerchantClick(adapterPosition)
            }
        }

        fun bind(position: Int) {
            with(itemView) {
                tv_merchant_name.setText("Cate $position")
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnMerchantClickListener {
        fun onMerchantClick(position: Int)
    }
}