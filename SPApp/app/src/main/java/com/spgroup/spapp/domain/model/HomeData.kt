package com.spgroup.spapp.domain.model

import java.io.Serializable

data class HomeData(
        val categories: List<TopLevelCategory>,
        val promotions: List<TopLevelPromotion>,
        val featuredPartners: List<TopLevelFeaturedPartner>
) : Serializable