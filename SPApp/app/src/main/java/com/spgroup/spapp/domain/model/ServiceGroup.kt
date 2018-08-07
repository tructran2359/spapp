package com.spgroup.spapp.domain.model

import java.io.Serializable

/**
 * Service that a Category has
 */
data class ServiceGroup(
        val id: Int = 0,
        var name: String,
        var description: String,
        var listItem: MutableList<ServiceItem>,
        var expanded: Boolean = false
): Serializable {

    fun clone() = ServiceGroup(id, name, description, listItem, expanded)

}