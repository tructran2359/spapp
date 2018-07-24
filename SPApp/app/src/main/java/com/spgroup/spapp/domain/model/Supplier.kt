package com.spgroup.spapp.domain.model

import java.io.Serializable

data class Supplier(
        val id: Int = -1,
        val name: String,
        val price: Float = 0f,
        val unit: String = "",
        val imgUrl: String = "",
        val isSponsored: Boolean = false,
        val isPromotion: Boolean = false
): Serializable