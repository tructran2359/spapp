package com.spgroup.spapp.domain.model

/**
 * Service that a Category has
 */
data class CategoryService(
        var name: String,
        var listItem: MutableList<ServiceItem>,
        var expanded: Boolean = false
)

data class ServiceItem(
        var name: String,
        var price: Int,
        var unit: String,
        var minCount: Int = 0,
        var maxCount: Int = 0,
        var count: Int = 0
)