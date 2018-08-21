package com.spgroup.spapp.repository.entity

import java.util.*

data class TopLevelPromotionEntity(
        val image: String,
        val promoText: String,
        val partnerName: String,
        val partnerId: String,
        val partnerType: String,
        val start: Date,
        val end: Date
)