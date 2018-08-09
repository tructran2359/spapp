package com.spgroup.spapp.domain.model

import java.io.Serializable

data class Partner(
        val uen: String = "",
        val name: String = "",
        val imgUrl: String = "",
        val priceDescription: String = "",
        val highlight: String = ""
) : Serializable, PartnersListingItem