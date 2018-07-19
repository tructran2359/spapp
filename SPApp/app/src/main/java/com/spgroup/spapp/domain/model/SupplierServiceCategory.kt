package com.spgroup.spapp.domain.model

import java.io.Serializable

data class SupplierServiceCategory(
        val id: Int,
        val type: Int = TYPE_LAUNDRY,
        val title: String,
        val services: List<ServiceGroup>): Serializable {

    companion object {
        val TYPE_LAUNDRY = 1
        val TYPE_WEEKLY_MENU = 2
    }

    fun getSelectedCount(): Int {
        var count = 0
        services.forEach {
            it.listItem.forEach {
                count += it.getItemCount()
            }
        }
        return count
    }

    fun getServiceItem(servicePos: Int, itemPos: Int) = services[servicePos].listItem[itemPos]
}