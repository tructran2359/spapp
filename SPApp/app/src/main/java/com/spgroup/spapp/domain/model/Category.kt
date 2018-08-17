package com.spgroup.spapp.domain.model

import java.io.Serializable

data class Category(
        val id: String,
        val label: String,
        val subCategories: List<SubCategory>
) : Serializable