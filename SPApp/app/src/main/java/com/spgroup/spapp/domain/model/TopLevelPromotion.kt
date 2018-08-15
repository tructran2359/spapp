package com.spgroup.spapp.domain.model

import java.io.Serializable

data class TopLevelPromotion(
        val imageUrl: String,
        val promoText: String,
        val partnerName: String,
        val partnerId: String,
        val startEnd: List<String>
) : Serializable