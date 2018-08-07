package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.NormalizedSummaryData
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.domain.model.ServiceItem

class GetOrderSummaryUsecase: SynchronousUsecase() {
    
    fun normalizeSummaryData(listCategory: List<ServiceCategory>): List<NormalizedSummaryData> {
        val mutableDataList = mutableListOf<NormalizedSummaryData>()

        listCategory.forEach {
            val cateName = it.title
            val cateId = it.id
            val listService = mutableListOf<ServiceItem>()

            it.services.forEach {
                listService.addAll(it.listItem)
            }

            mutableDataList.add(NormalizedSummaryData(cateName, cateId, listService))
        }

        return mutableDataList
    }
}