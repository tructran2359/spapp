package com.spgroup.spapp.repository.entity

data class PartnersListingResponse(
        val partners: List<PartnerEntity>,
        val promotions: List<PromotionEntity>
)