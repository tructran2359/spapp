package com.spgroup.spapp.domain.model

import java.io.Serializable

data class Supplier(
        val id: Int = -1,
        val price: Float = 0f,
        val unit: String = "",
        val isSponsored: Boolean = false,
        val isPromotion: Boolean = false,

        val uen: String = "",
        val name: String = "",
        val imgUrl: String = "",
        val priceDescription: String = "",
        val highlight: String = ""
): Serializable