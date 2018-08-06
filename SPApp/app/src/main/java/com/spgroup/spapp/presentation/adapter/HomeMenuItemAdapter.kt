package com.spgroup.spapp.presentation.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import com.spgroup.spapp.presentation.adapter.diff_utils.HomeMenuDiffCallback
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.menu_item.view.*

class HomeMenuItemAdapter: RecyclerView.Adapter<HomeMenuItemAdapter.HomeMenuVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var mData = mutableListOf<TopLevelServiceCategory>()
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

    fun setData(data: List<TopLevelServiceCategory>) {
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

        fun bind(item: TopLevelServiceCategory) {
            with(itemView) {
                val imageId = when (item.id) {
                    1 -> R.drawable.menu_food
                    2 -> R.drawable.menu_housekeeping
                    3 -> R.drawable.menu_aircon
                    4 -> R.drawable.menu_laundry
                    5 -> R.drawable.menu_education
                    6 -> R.drawable.menu_grocery
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