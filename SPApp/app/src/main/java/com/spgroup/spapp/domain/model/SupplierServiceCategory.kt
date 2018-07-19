package com.spgroup.spapp.domain.model

import java.io.Serializable

data class SupplierServiceCategory(val id: Int, val title: String, val services: List<ServiceGroup>): Serializable {

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