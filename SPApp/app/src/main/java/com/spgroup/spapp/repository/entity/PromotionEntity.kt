package com.spgroup.spapp.repository.entity

data class PromotionEntity(
        val image: String,
        val promoText: String,
        val partnerName: String,
        val start: String?,
        val end: String?,
        val partnerId: String,
        val partnerType: String

//        val partnerId: String,
//        val startEnd: List<String>
)