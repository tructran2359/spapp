package com.spgroup.spapp.domain.model

import com.google.gson.JsonElement

sealed class AbsServiceItem


data class ComplexCustomisationService(
        val id: String,
        val label: String,
        val serviceDescription: String,
        val customisations: JsonElement //TODO truc, define model later
) : AbsServiceItem()


data class CheckboxService(
        val id: String,
        val label: String,
        val serviceDescription: String,
        val priceText: String
) : AbsServiceItem()


data class MultiplierService(
        val id: String,
        val label: String,
        val price: Float,
        val min: Int,
        val max: Int,
        val unit: String
) : AbsServiceItem()