package com.spgroup.spapp.domain.model

data class SubCategory(
        val id: String,
        val label: String,
        val description: String,
        val services: List<Service>
)