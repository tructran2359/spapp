package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.usecase.SelectedServiceUsecase
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData

class OrderSummaryViewModel(): ViewModel() {

    val mDeletedCateId = MutableLiveData<String>()
    val mDeletedServiceId = MutableLiveData<Int>()
    val mEmpty = MutableLiveData<Boolean>()
    val mTotalCount = MutableLiveData<Int>()
    val mEstPrice = MutableLiveData<Float>()
    val mUpdatedComplexService = MutableLiveData<ComplexSelectedService>()


    private lateinit var mMapCateInfo: HashMap<String, String>
    var mMapSelectedServices = MutableLiveData<HashMap<String, MutableList<ISelectedService>>>()
    private val mSelectedServiceUsecase = SelectedServiceUsecase()

    fun initData(mapCateInfo: HashMap<String, String>, mapSelectedServices: HashMap<String, MutableList<ISelectedService>>) {
        mMapCateInfo = mapCateInfo
        mMapSelectedServices.value = mapSelectedServices
        mSelectedServiceUsecase.mapSelectedServices = mapSelectedServices
        updateCountAndPrice()
    }

    fun updateNormalSelectedServiceItem(
            absServiceItem: AbsServiceItem,
            count: Int,
            categoryId: String
    ) {
        mSelectedServiceUsecase.updateNormalSelectedServiceItem(
                absServiceItem,
                count,
                categoryId
        )

        updateCountAndPrice()
    }

    private fun updateCountAndPrice() {
        mTotalCount.value = mSelectedServiceUsecase.calculateTotalCount()
        mEstPrice.value = mSelectedServiceUsecase.calculateEstPrice()
    }


    fun deleteService(categoryId: String, serviceId: Int) {
        mSelectedServiceUsecase.removeSelectedItem(categoryId, serviceId)
        mDeletedServiceId.value = serviceId
        val listSelectedService = mSelectedServiceUsecase.getListSelectedService(categoryId)
        if (listSelectedService == null || listSelectedService.isEmpty()) {
            mDeletedCateId.value = categoryId
            mSelectedServiceUsecase.removeCategory(categoryId)
            if (mSelectedServiceUsecase.mapSelectedServices.isEmpty()) {
                mEmpty.value = true
            }
        }

        updateCountAndPrice()
    }

    fun getCateName(cateId: String) = mMapCateInfo[cateId]

    fun updateComplexSelectedServiceItem(customiseDisplayData: CustomiseDisplayData) {
        mSelectedServiceUsecase.updateComplexSelectedServiceItem(customiseDisplayData)
        val updatedService = mSelectedServiceUsecase.getSelectedService(
                customiseDisplayData.categoryId,
                customiseDisplayData.serviceItem.id)
        if (updatedService != null && updatedService is ComplexSelectedService) {
            mUpdatedComplexService.value = updatedService
        }
        updateCountAndPrice()
    }

}