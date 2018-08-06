package com.spgroup.spapp.presentation.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.presentation.adapter.diff_utils.HomeMenuDiffCallback
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.menu_item.view.*

class HomeMenuItemAdapter: RecyclerView.Adapter<HomeMenuItemAdapter.HomeMenuVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var mData = mutableListOf<TopLevelCategory>()
    var mListener: OnItemClickListener? = null

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMenuVH {
        val view = parent.inflate(R.layout.menu_item, false)
        return HomeMenuVH(view, mListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: HomeMenuVH, position: Int) {
        vh.bind(mData[position])
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setData(data: List<TopLevelCategory>) {
        val callback = HomeMenuDiffCallback(mData, data)
        val result = DiffUtil.calculateDiff(callback)
        mData.clear()
        mData.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class HomeMenuVH(itemView: View, val listener: OnItemClickListener?): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }

        fun bind(item: TopLevelCategory) {
            with(itemView) {
                val imageId = when (item.id) {
                    "food" -> R.drawable.menu_food
                    "housekeeping" -> R.drawable.menu_housekeeping
                    "aircon" -> R.drawable.menu_aircon
                    "laundry" -> R.drawable.menu_laundry
                    "education" -> R.drawable.menu_education
                    "groceries" -> R.drawable.menu_grocery
                    else -> throw IllegalArgumentException("ID ${item.id} not found")
                }
                iv_logo.setImageResource(imageId)
                tv_name.setText(item.name)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}