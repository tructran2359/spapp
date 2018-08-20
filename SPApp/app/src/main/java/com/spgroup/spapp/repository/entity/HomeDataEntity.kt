package com.spgroup.spapp.repository.entity

data class HomeDataEntity(
        val categories: List<TopLevelCategoryEntity>,
        val promotions: List<TopLevelPromotionEntity>,
        val featuredPartners: List<TopLevelFeaturedPartnerEntity>,
        val pages: List<TopLevelPageEntity>,
        val variables: TopLevelVariableEntity
)