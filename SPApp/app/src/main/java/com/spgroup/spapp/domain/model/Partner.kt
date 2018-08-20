package com.spgroup.spapp.domain.model

import java.io.Serializable

data class Partner(
        val uen: String = "",
        val name: String = "",
        val imgUrl: String = "",
        val priceDescription: String = "",
        val highlight: String = "",
        val partnerType: String = ""
) : Serializable, PartnersListingItem {

    fun isCart() = partnerType == "cart"

    fun isInfo() = partnerType == "info"

    fun isDetailInfo() = partnerType == "detail_info"
}