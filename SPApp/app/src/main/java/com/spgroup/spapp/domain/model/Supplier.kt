package com.spgroup.spapp.domain.model

data class Supplier(
        val id: Int,
        val name: String,
        val price: Float,
        val unit: String,
        val imgUrl: String,
        val isSponsored: Boolean)