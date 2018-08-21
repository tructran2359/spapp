package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.usecase.GetCustomisationLowestPrice
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.formatPrice

class PartnerDetailsViewModel(
        private val getServicesListByPartnerUsecase: GetServicesListByPartnerUsecase,
        private val getCustomisationLowestPrice: GetCustomisationLowestPrice
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
                            preProcessCustomisationLowestPrice(it)
                            partnerDetails.value = it
                        },
                        { error.value = it }
                )
        disposeBag.add(disposable)
    }

    private fun preProcessCustomisationLowestPrice(partnerDetails: PartnerDetails) {
        partnerDetails.categories?.forEach { cat ->
            cat.subCategories.forEach { subCat ->
                subCat.services.forEach { service ->
                    if (service is ComplexCustomisationService) {
                        val lowestPrice = getCustomisationLowestPrice.run(service.customisations)
                        if (lowestPrice == GetCustomisationLowestPrice.NO_PRICE) {
                            service.priceText = null
                        } else {
                            service.priceText = lowestPrice.formatPrice()
                        }

                    }
                }
            }
        }
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

    fun getPartnerInfoModel(): PartnerInformationActivity.PartnerInfo? {
        partnerDetails.value?.run {
            return PartnerInformationActivity.PartnerInfo(
                    name = name,
                    desc = description,
                    offerTitle = offeringTitle,
                    offers = offering,
                    phone = phone,
                    uen = uen,
                    nea = nea,
                    website = website,
                    tnc = tnc
            )
        }

        return null
    }
}

data class SelectedValueItem(
        var serviceId: Int,
        var count: Int = 0,
        var subTotal: Float = 0F
)