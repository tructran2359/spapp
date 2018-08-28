package com.spgroup.spapp.domain.model

data class OrderSummary(
        val timestamp: Long,
        val partnerId: String,
        val orders: List<Order>,
        val orderDiscountPercentage: Int,
        val overallCostBeforeDiscount: Float,
        val overallCostAfterDiscount: Float,
        val contactInfo: ContactInfo
)