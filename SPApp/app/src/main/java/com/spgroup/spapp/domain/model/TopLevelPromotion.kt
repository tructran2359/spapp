package com.spgroup.spapp.domain.model

import java.io.Serializable
import java.util.*

data class TopLevelPromotion(
        val imageUrl: String,
        val promoText: String,
        val partnerName: String,
        val partnerType: String,
        val partnerId: String,
        val start: Date,
        val end: Date
) : Serializable