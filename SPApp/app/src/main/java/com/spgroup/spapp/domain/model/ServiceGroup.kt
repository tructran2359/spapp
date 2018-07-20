package com.spgroup.spapp.domain.model

import java.io.Serializable

/**
 * Service that a Category has
 */
data class ServiceGroup(
        var name: String,
        var description: String,
        var listItem: MutableList<ServiceItem>,
        var expanded: Boolean = false
): Serializable {

    fun clone() = ServiceGroup(name, description, listItem, expanded)

}