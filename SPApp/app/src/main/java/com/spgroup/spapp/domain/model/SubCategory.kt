package com.spgroup.spapp.domain.model

import java.io.Serializable

data class SubCategory(
        val id: String,
        val label: String,
        val description: String,
        val services: List<AbsServiceItem>
): Serializable