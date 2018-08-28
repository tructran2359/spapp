package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.model.ContactInfo
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.usecase.GetOrderSummaryUsecase
import com.spgroup.spapp.domain.usecase.SelectedServiceUsecase
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData

class OrderSummaryViewModel(
        private val mSelectedServiceUsecase: SelectedServiceUsecase,
        private val mGetOrderSummaryUsecase: GetOrderSummaryUsecase
): ViewModel() {

    val mDeletedCateId = MutableLiveData<String>()
    val mDeletedServiceId = MutableLiveData<Int>()
    val mEmpty = MutableLiveData<Boolean>()
    val mTotalCount = MutableLiveData<Int>()
    val mEstPrice = MutableLiveData<EstPriceData>()
    val mUpdatedComplexService = MutableLiveData<ComplexSelectedService>()

    var mMapSelectedServices = MutableLiveData<HashMap<String, MutableList<ISelectedService>>>()
    private lateinit var mPartnerDetails: PartnerDetails

    fun initData(
            partnerDetails: PartnerDetails,
            mapSelectedServices: HashMap<String, MutableList<ISelectedService>>) {
        mPartnerDetails = partnerDetails
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
        mEstPrice.value = EstPriceData(mPartnerDetails.getDiscountValue(), mSelectedServiceUsecase.calculateEstPrice())
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

    fun getCateName(cateId: String) = mPartnerDetails.getCategoryById(cateId)?.label

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

    fun getPartnerName() = mPartnerDetails.name

    fun getOrderSummaryModel(contactInfo: ContactInfo) = mGetOrderSummaryUsecase.getOrderSummaryModel(
            mPartnerDetails,
            mSelectedServiceUsecase.mapSelectedServices,
            contactInfo,
            mSelectedServiceUsecase.calculateEstPrice()
    )

}

data class EstPriceData(
        val discount: Float,
        val originalPrice: Float
)