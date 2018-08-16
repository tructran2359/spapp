package com.spgroup.spapp.domain.model

import com.google.gson.JsonElement

sealed class AbsServiceItem {
    abstract fun getServiceId(): Int
}


data class ComplexCustomisationService(
        val id: Int,
        val label: String,
        val serviceDescription: String,
        val customisations: JsonElement //TODO truc, define model later
) : AbsServiceItem() {
    override fun getServiceId() = id
}


data class CheckboxService(
        val id: Int,
        val label: String,
        val serviceDescription: String,
        val priceText: String
) : AbsServiceItem() {
    override fun getServiceId() = id
}


data class MultiplierService(
        val id: Int,
        val label: String,
        val price: Float,
        val min: Int,
        val max: Int,
        val unit: String
) : AbsServiceItem() {
    override fun getServiceId() = id
}