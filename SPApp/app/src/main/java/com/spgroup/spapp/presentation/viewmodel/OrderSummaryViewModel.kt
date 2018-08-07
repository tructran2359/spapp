package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.NormalizedSummaryData
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.domain.usecase.GetOrderSummaryUsecase

class OrderSummaryViewModel(val usecase: GetOrderSummaryUsecase): ViewModel() {

    val mListNormalizedData = MutableLiveData<MutableList<NormalizedSummaryData>>()
    val mDeletedCateId = MutableLiveData<Int>()
    val mDeletedServiceId = MutableLiveData<Int>()
    val mEmpty = MutableLiveData<Boolean>()

    fun initData(listCate: List<ServiceCategory>) {
        mListNormalizedData.value = usecase.normalizeSummaryData(listCate).toMutableList()
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

    fun deleteService(id: Int) {
        mListNormalizedData.value?.run {
            for (data in this) {
                for (service in data.listService) {
                    if (service.id == id) {
                        data.listService.remove(service)
                        mDeletedServiceId.value = service.id
                        if (data.listService.isEmpty()) {
                            this.remove(data)
                            mDeletedCateId.value = data.cateId
                        }

                        if (this.isEmpty()) {
                            mEmpty.value = true
                        }
                        return
                    }
                }
            }
        }
    }

}