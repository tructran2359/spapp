package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.MultiplierService
import com.spgroup.spapp.domain.model.SubCategory
import com.spgroup.spapp.presentation.adapter.viewholder.ServiceVH
import com.spgroup.spapp.util.extension.inflate
import com.spgroup.spapp.util.extension.toInt
import kotlinx.android.synthetic.main.layout_service.view.*
import org.jetbrains.anko.collections.forEachWithIndex

class ServiceListingAdapter(
        private val mItemDelegate: OnItemInteractedListener
) : RecyclerView.Adapter<ServiceVH>() {

    companion object {
        const val VIEW_TYPE_LAST = 1
        const val VIEW_TYPE_NORMAL = 2
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private val mData = mutableListOf<SubCategory>()
    private val mMapExpandedItem = mutableMapOf<String, Boolean>() //<SubCatId, isExpanded>
    private val mMapSelectedValue = mutableMapOf<Int, Int>() //<ServiceId, Count>

    private val internalItemListener = object : OnItemInteractedListener {
        override fun onComplexCustomisationItemClick(itemData: ComplexCustomisationService) {
            mItemDelegate.onComplexCustomisationItemClick(itemData)
        }

        override fun onComplexCustomisationItemDelete(itemData: ComplexCustomisationService) {
            mMapSelectedValue[itemData.id] = 0
            mItemDelegate.onComplexCustomisationItemDelete(itemData)
        }

        override fun onMultiplierItemChanged(itemData: MultiplierService, count: Int) {
            mMapSelectedValue[itemData.id] = count
            mItemDelegate.onMultiplierItemChanged(itemData, count)
        }

        override fun onCheckboxItemChanged(itemData: CheckboxService, checked: Boolean) {
            mMapSelectedValue[itemData.id] = checked.toInt()
            mItemDelegate.onCheckboxItemChanged(itemData, checked)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceVH {
        val view = parent.inflate(R.layout.layout_service)
        view.v_devider_light.isGone = (viewType == VIEW_TYPE_LAST)
        return ServiceVH(view, internalItemListener, this::collapseItem)
    }

    override fun onBindViewHolder(vh: ServiceVH, position: Int) {
        vh.bind(mData[position], mMapSelectedValue, isItemExpanded(position), mData.size == 1)
    }

    override fun getItemCount() = mData.size

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

    fun refreshDataState() {
        mMapSelectedValue.clear()
        mMapExpandedItem.clear()
        notifyDataSetChanged()
    }

    private fun collapseItem(position: Int) {
        val expanded = isItemExpanded(position)
        mMapExpandedItem[mData[position].id] = expanded.not()
        notifyItemChanged(position)
    }

    private fun isItemExpanded(position: Int): Boolean {
        val subCatId = mData[position].id
        return mMapExpandedItem[subCatId] ?: false
    }

    fun addSelectedItem(serviceId: Int, count: Int) {
        mMapSelectedValue[serviceId] = count
        mData.forEachWithIndex { index, subCate ->
            val service = subCate.services.firstOrNull { it.getServiceId() == serviceId }
            if (service != null) {
                notifyItemChanged(index)
                return
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnItemInteractedListener {
        fun onComplexCustomisationItemClick(itemData: ComplexCustomisationService)

        fun onComplexCustomisationItemDelete(itemData: ComplexCustomisationService)

        fun onMultiplierItemChanged(itemData: MultiplierService, count: Int)

        fun onCheckboxItemChanged(itemData: CheckboxService, checked: Boolean)
    }

}