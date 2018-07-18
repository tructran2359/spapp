package com.spgroup.spapp.domain.model

/**
 * Service that a Category has
 */
data class ServiceGroup(
        var name: String,
        var description: String,
        var listItem: MutableList<ServiceItem>,
        var expanded: Boolean = false
)