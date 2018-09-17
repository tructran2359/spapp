package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.model.ContactInfo
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.model.RequestAck
import com.spgroup.spapp.domain.usecase.GetOrderSummaryUsecase
import com.spgroup.spapp.domain.usecase.SelectedServiceUsecase
import com.spgroup.spapp.domain.usecase.SubmitRequestUsecase
import com.spgroup.spapp.manager.AppConfigManager
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import com.spgroup.spapp.util.doLogD
import javax.inject.Inject

class OrderSummaryViewModel @Inject constructor(
        private val mSubmitRequestUsecase: SubmitRequestUsecase,
        private val mSelectedServiceUsecase: SelectedServiceUsecase,
        private val mGetOrderSummaryUsecase: GetOrderSummaryUsecase,
        private val mAppConfigmanager: AppConfigManager
): BaseViewModel() {

    val mDeletedCateId = MutableLiveData<String>()
    val mDeletedServiceId = MutableLiveData<Int>()
    val mEmpty = MutableLiveData<Boolean>()
    val mTotalCount = MutableLiveData<Int>()
    val mEstPrice = MutableLiveData<EstPriceData>()
    val mUpdatedComplexService = MutableLiveData<ComplexSelectedService>()
    val mIsLoading = MutableLiveData<Boolean>()
    val mRequestAck = MutableLiveData<RequestAck>()

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
        val discountValue = mPartnerDetails.getDiscountValue()
        val originalPrice = mSelectedServiceUsecase.calculateEstPrice()
        val minimumOrderAmount = mPartnerDetails.getMinimumOrderValue()
        val surcharge = if (minimumOrderAmount > originalPrice) {
            minimumOrderAmount - originalPrice
        } else {
            0f
        }
        val amountDiscount = mPartnerDetails.getAmountDiscountValue()
        val amountDiscountLabel = mPartnerDetails.amountDiscountLabel ?: ""
        mEstPrice.value = EstPriceData(discountValue, originalPrice, surcharge, amountDiscount, amountDiscountLabel, minimumOrderAmount)
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

    fun getSubCateName(categoryId: String, serviceId: Int) = mPartnerDetails.getSubCateByCateIdAndServiceId(categoryId, serviceId)?.label ?: ""

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

    fun submitRequest(contactInfo: ContactInfo) {
        val orderSummary = getOrderSummaryModel(contactInfo)
        doLogD("Summary", "Submit:\n${Gson().toJson(orderSummary)}")
        val disposable = mSubmitRequestUsecase
                .submitRequest(orderSummary)
                .doOnSubscribe { mIsLoading.value = true  }
                .subscribe(
                        { requestAck ->
                            mIsLoading.value = false
                            mRequestAck.value = requestAck

                            // Simulate api call error
//                            error.value = Throwable("Test API error Order Summary")
                        },
                        { throwable ->
                            mIsLoading.value = false
                            error.value = throwable
                        }
                )
        disposeBag.addAll(disposable)
    }

    fun saveContactInfo(contactInfo: ContactInfo) {
        // Do not cache `Notes`
        val savedContactInfo = contactInfo.copy(notes = "")
        mAppConfigmanager.setRememberMe(savedContactInfo)
    }

    fun removeSavedContactInfo() {
        mAppConfigmanager.clearRememberMe()
    }

    fun getSavedContactInfo() = mAppConfigmanager.getSavedContactInfo()

    fun isRemembered() = mAppConfigmanager.isRemembered()

    fun getSelectedServicesMap() = mSelectedServiceUsecase.mapSelectedServices

}

data class EstPriceData(
        val discountPercentage: Float,
        val originalPrice: Float,
        val surcharge: Float,
        val amountDiscount: Float,
        val amountDiscountLabel: String,
        val minimumOrderAmount: Float
)