package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class OrderSummaryViewModel(): ViewModel() {

    val mDeletedCateId = MutableLiveData<String>()
    val mDeletedServiceId = MutableLiveData<Int>()
    val mEmpty = MutableLiveData<Boolean>()
//    val mListNormalizedData = MutableLiveData<MutableList<NormalizedSummaryData>>()


    private lateinit var mMapCateInfo: HashMap<String, String>
    var mMapSelectedServices = MutableLiveData<HashMap<String, MutableList<ISelectedService>>>()

    fun initData(mapCateInfo: HashMap<String, String>, mapSelectedServices: HashMap<String, MutableList<ISelectedService>>) {
        mMapCateInfo = mapCateInfo
        mMapSelectedServices.value = mapSelectedServices
    }

//    fun getServiceById(id: Int): ISelectedService? {
//        mListNormalizedData.value?.run {
//            for (data in this) {
//                for (selectedService in data.listSelectedService) {
//                    if (selectedService.getId() == id) {
//                        return selectedService
//                    }
//                }
//            }
//        }
//        return null
//    }

    fun deleteService(id: Int) {
//        mListNormalizedData.value?.run {
//            for (data in this) {
//                for (selectedService in data.listSelectedService) {
//                    if (selectedService.getId() == id) {
//                        data.listSelectedService.remove(selectedService)
//                        mDeletedServiceId.value = selectedService.getId()
//                        if (data.listSelectedService.isEmpty()) {
//                            this.remove(data)
//                            mDeletedCateId.value = data.cateId
//                        }
//
//                        if (this.isEmpty()) {
//                            mEmpty.value = true
//                        }
//                        return
//                    }
//                }
//            }
//        }
    }

    fun getCateName(cateId: String) = mMapCateInfo[cateId]

}