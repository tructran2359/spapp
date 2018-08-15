package com.spgroup.spapp.repository.entity

data class TopLevelPromotionEntity(
        val image: String,
        val promoText: String,
        val partnerName: String,
        val partnerId: String,
        val startEnd: List<String>
)