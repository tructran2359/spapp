package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase
import com.spgroup.spapp.util.doLogD

class PartnerDetailsViewModel(
        private val getServicesListByPartnerUsecase: GetServicesListByPartnerUsecase
) : BaseViewModel() {

    lateinit var partnerUEN: String
    private val mapSelectedValue = mutableMapOf<String, MutableList<SelectedValueItem>>()

    val partnerDetails = MutableLiveData<PartnerDetails>()
    val selectedCount = MutableLiveData<Int>()

    init {
        selectedCount.value = 0
    }

    fun loadServices() {
        val disposable = getServicesListByPartnerUsecase
                .getPartnerDetails(partnerUEN)
                .subscribe(
                        {
                            partnerDetails.value = it
                        },
                        { error.value = it }
                )
        disposeBag.add(disposable)
    }

    fun updateSelectedServiceCategories(count: Int, categoryId: String, serviceId: Int) {
        doLogD(msg = "count:$count catId:$categoryId serId:$serviceId")
        var selectedValueList = mapSelectedValue[categoryId]
        if (selectedValueList != null) {
            var existed = false
            selectedValueList.forEach { valueItem ->
                if (valueItem.serviceId == serviceId) {
                    existed = true
                    valueItem.count = count
                }
            }
            if (!existed) {
                selectedValueList.add(SelectedValueItem(serviceId, count))
            }
        } else {
            val valueItem = SelectedValueItem(serviceId, count)
            selectedValueList = mutableListOf(valueItem)
        }
        mapSelectedValue[categoryId] = selectedValueList

        selectedCount.value = mapSelectedValue
                .map { it.value } // to list map of List<SelectedValueItem>
                .flatten() // to list of all List<SelectedValueItem>
                .map { it.count } // to list of SelectedValueItem#count
                .sum() // sum of SelectedValueItem#count
    }

    fun getCategory(categoryId: String): Category? {
        return partnerDetails
                .value
                ?.categories
                ?.first { it.id == categoryId }
    }
}

data class SelectedValueItem(
        var serviceId: Int,
        var count: Int = 0,
        var subTotal: Float = 0F
)