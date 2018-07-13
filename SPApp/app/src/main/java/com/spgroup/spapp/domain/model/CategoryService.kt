package com.spgroup.spapp.domain.model

/**
 * Service that a Category has
 */
data class CategoryService(
        var name: String,
        var listItem: MutableList<ServiceItem>,
        var expanded: Boolean = false
)