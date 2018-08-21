package com.spgroup.spapp.domain.model

import java.io.Serializable

data class TopLevelFeaturedPartner(
        val id: String,
        val name: String,
        val category: String,
        val logoUrl: String,
        val partnerType: String
) : Serializable