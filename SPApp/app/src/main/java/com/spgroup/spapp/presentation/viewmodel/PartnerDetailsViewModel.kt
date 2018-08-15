package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase

class PartnerDetailsViewModel(
        private val getServicesListByPartnerUsecase: GetServicesListByPartnerUsecase
) : BaseViewModel() {

    lateinit var partnerUEN: String
    private val mapSelectedValue = mutableMapOf<String, List<SelectedValueItem>>()

    val partnerDetails = MutableLiveData<PartnerDetails>()
    val selectedCount = MutableLiveData<Int>()

    init {
        selectedCount.value = 0
    }

    fun loadServices() {
        val disposable = getServicesListByPartnerUsecase
                .getPartnerDetails(partnerUEN)
                .subscribe(
                        { partnerDetails.value = it },
                        { error.value = it }
                )
        disposeBag.add(disposable)
    }

    fun updateSelectedServiceCategories(count: Int, categoryId: String, serviceId: String) {
        var selectedValueList = mapSelectedValue[categoryId]
        if (selectedValueList != null) {
            selectedValueList.forEach { valueItem ->
                if (valueItem.serviceId == serviceId) {
                    valueItem.count = count
                }
            }
        } else {
            val valueItem = SelectedValueItem(serviceId, count)
            selectedValueList = listOf(valueItem)
        }
        mapSelectedValue[categoryId] = selectedValueList

        selectedCount.value = mapSelectedValue
                .map { it.value } // to list map of List<SelectedValueItem>
                .flatten() // to list of all List<SelectedValueItem>
                .map { it.count } // to list of SelectedValueItem#count
                .sum() // sum of SelectedValueItem#count
    }
}

data class SelectedValueItem(
        var serviceId: String,
        var count: Int = 0,
        var subTotal: Float = 0F
)