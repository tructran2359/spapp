package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.NormalizedSummaryData
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.domain.usecase.GetOrderSummaryUsecase

class OrderSummaryViewModel(val usecase: GetOrderSummaryUsecase): ViewModel() {

    val mListNormalizedData = MutableLiveData<List<NormalizedSummaryData>>()

    fun initData(listCate: List<ServiceCategory>) {
        mListNormalizedData.value = usecase.normalizeSummaryData(listCate)
    }

    fun getServiceById(id: Int): ServiceItem? {
        mListNormalizedData.value?.run {
            for (data in this) {
                for (service in data.listService) {
                    if (service.id == id) {
                        return service
                    }
                }
            }
        }
        return null
    }

}