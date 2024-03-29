package com.spgroup.spapp.domain.model

data class OrderSummary(
        val timestamp: Long,
        val partnerId: String,
        val orders: List<Order>,
        val orderDiscountPercentage: Float,
        val overallCostBeforeDiscount: Float,
        val overallCostAfterDiscount: Float,
        val contactInfo: ContactInfo,
        val surcharge: Float,
        val amountDiscount: Float
)