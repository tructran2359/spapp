package com.spgroup.spapp.domain.model

data class Order(
        val serviceType: String,
        val serviceCategoryName: String,
        val serviceSubcategoryName: String,
        val itemId: Int,
        val itemName: String,
        val itemQty: Int,
        val itemTotalPrice: Float, //Nullable coz Checkbox does not impact price
        val itemCommentsNotes: String?,  // Nullable coz only Complex has this value
        val optionValue: String?  // Nullable coz this is only used for Complex
)