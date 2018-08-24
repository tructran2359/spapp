package com.spgroup.spapp.domain.model

data class Promotion(
        val imagePath: String,
        val promoText: String,
        val partnerName: String,
        val start: String?,
        val end: String?,
        val partnerId: String,
        val partnerType: String
) : PartnersListingItem