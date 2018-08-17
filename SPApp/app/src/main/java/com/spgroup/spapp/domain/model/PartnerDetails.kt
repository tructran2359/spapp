package com.spgroup.spapp.domain.model

data class PartnerDetails(
        val uen: String,
        val nea: String,
        val id: String,
        val name: String,
        val categoryId: String,
        val partnerType: String,
        val description: String,
        val promo: String,
        val phone: String,
        val menuTitle: String,
        val menuSummary: String,
        val website: String,
        val offeringTitle: String,
        val offering: List<String>,
        val banners: List<String>,
        val categories: List<Category>,
        val menus: List<FoodMenu>
)