package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.SubCategory
import com.spgroup.spapp.presentation.adapter.viewholder.ServiceVH
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_service.view.*

class ServiceListingAdapter(private val mItemInteractedListener: OnItemInteractedListener) : RecyclerView.Adapter<ServiceVH>() {

    companion object {
        const val VIEW_TYPE_LAST = 1
        const val VIEW_TYPE_NORMAL = 2
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private val mData = mutableListOf<SubCategory>()
    private val mMapExpandedItem = mutableMapOf<String, Boolean>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceVH {
        val view = parent.inflate(R.layout.layout_service)
        view.v_devider_light.isGone = (viewType == VIEW_TYPE_LAST)
        return ServiceVH(view, mItemInteractedListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(vh: ServiceVH, position: Int) {
        vh.bind(mData[position], isItemExpanded(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mData.size - 1) VIEW_TYPE_LAST else VIEW_TYPE_NORMAL
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun submitData(data: List<SubCategory>?) {
        mData.clear()
        data?.let {
            mData.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun collapseItem(position: Int) {
        val expanded = isItemExpanded(position)
        mMapExpandedItem[mData[position].id] = expanded.not()
        notifyItemChanged(position)
    }

//    fun getItem(servicePos: Int, itemPos: Int) = mData[servicePos].listItem[itemPos]

    private fun isItemExpanded(position: Int): Boolean {
        val subCatId = mData[position].id
        return mMapExpandedItem[subCatId] ?: false
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnItemInteractedListener {
        fun onCollapseClick(position: Int)

        fun onServiceItemClick(servicePos: Int, itemPos: Int)

        fun onCountChanged(count: Int, servicePos: Int, itemPos: Int)

        fun onCheckChanged(checked: Boolean, servicePos: Int, itemPos: Int)
    }

}