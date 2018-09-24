package com.spgroup.spapp.domain.model

data class Order(
        val itemCommentsNotes: String?,  // Nullable coz only Complex has this value
        val itemId: Int,
        val itemName: String,
        val itemOptions: List<OrderOption>?,
        val itemQty: Int,
        val itemTotalPrice: Float, //Nullable coz Checkbox does not impact price,,,,
        val itemUnit: String?,
        val serviceCategoryName: String,
        val serviceSubcategoryName: String,
        val serviceType: String
)

data class OrderOption(
        val itemName: String,
        val itemPrice: Float
)