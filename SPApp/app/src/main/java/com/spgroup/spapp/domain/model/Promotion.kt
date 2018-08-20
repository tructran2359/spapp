package com.spgroup.spapp.domain.model

data class Promotion(
        val imagePath: String,
        val promoText: String,
        val partnerName: String

//        val partnerId: String,
//        val startEnd: List<String>

) : PartnersListingItem