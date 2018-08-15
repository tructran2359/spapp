package com.spgroup.spapp.domain.model

data class Service(
        val id: String,
        val serviceType: String,
        val label: String,
        val serviceDescription: String,
        val priceText: String
)