package com.spgroup.spapp.domain.model

data class NormalizedSummaryData (
        val cateName: String,
        val cateId: Int,
        val listService: MutableList<ServiceItem>
) {

    fun needHeader(): Boolean {
        for (serviceItem in listService) {
            if (serviceItem !is ServiceItemComboDummy) {
                return true
            }
        }

        return false
    }
}