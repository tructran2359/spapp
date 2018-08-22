package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.*
import com.spgroup.spapp.domain.usecase.GetCustomisationLowestPrice
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.formatPrice
import com.spgroup.spapp.util.extension.toInt
import java.io.Serializable

class PartnerDetailsViewModel(
        private val getServicesListByPartnerUsecase: GetServicesListByPartnerUsecase,
        private val getCustomisationLowestPrice: GetCustomisationLowestPrice
) : BaseViewModel() {

    lateinit var partnerUEN: String
    val mapSelectedServices = HashMap<String, MutableList<ISelectedService>>()

    val partnerDetails = MutableLiveData<PartnerDetails>()
    val selectedCount = MutableLiveData<Int>()
    val estimatedPrice = MutableLiveData<Float>()

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

    /**
     * Update when Multiplier or Checkbox card is selected/unselected
     * Throw exception when called with Complex card
     */
    fun updateNormalSelectedServiceItem(absServiceItem: AbsServiceItem, count: Int, categoryId: String) {
        doLogD(msg = "count:$count catId:$categoryId serId:${absServiceItem.getServiceId()}")

        var selectedValueList = mapSelectedServices[categoryId]
        if (count == 0) {
            removeSelectedItem(selectedValueList, absServiceItem.getServiceId())
        } else {
            addNormalSelectedItem(absServiceItem, count, selectedValueList, categoryId)
        }
        updateCountAndPrice()
    }

    private fun addNormalSelectedItem(
            absServiceItem: AbsServiceItem,
            count: Int,
            selectedValueList: MutableList<ISelectedService>?,
            categoryId: String) {
        var calculatingList = selectedValueList
        val subTotal = when (absServiceItem) {

            is MultiplierService -> count * absServiceItem.price

            is CheckboxService -> 0f

            is ComplexCustomisationService -> throw IllegalArgumentException("Should not call with Complex card")
        }

        if (calculatingList != null) {
            var existed = false
            calculatingList.forEach { valueItem ->
                (valueItem as SelectedService).run {
                    if (service.getServiceId() == absServiceItem.getServiceId()) {
                        existed = true
                        this.count = count
                        this.subTotal = subTotal
                    }
                }
            }
            if (!existed) {
                calculatingList.add(SelectedService(absServiceItem, count, subTotal))
            }
        } else {
            val valueItem = SelectedService(absServiceItem, count, subTotal)
            calculatingList = mutableListOf(valueItem)
        }
        mapSelectedServices[categoryId] = calculatingList
    }

    private fun removeSelectedItem(selectedValueList: MutableList<ISelectedService>?, serviceId: Int) {
        var deletedPos = -1
        selectedValueList?.forEachIndexed { index, item ->
            if (item.getId() == serviceId) {
                deletedPos = index
                return@forEachIndexed
            }
        }
        if (deletedPos != -1) {
            selectedValueList?.removeAt(deletedPos)
        }
    }

    private fun updateCountAndPrice() {
        selectedCount.value = mapSelectedServices
                .map { it.value } // to list map of List<ISelectedValueItem>
                .flatten() // to list of all List<ISelectedValueItem>
                .map { it.getSelectedCount() } // to list of SelectedValueItem#count
                .sum() // sum of SelectedValueItem#count

        estimatedPrice.value = mapSelectedServices
                .map { it.value }
                .flatten()
                .map { it.getEstPrice() }
                .sum()
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

    fun getSelectedOptionMap(categoryId: String, serviceId: Int): HashMap<Int, Int>? {
        return getSelectedService(categoryId, serviceId)?.let {
            (it as ComplexSelectedService).selectedCustomisation
        }
    }

    fun getSelectedInstruction(categoryId: String, serviceId: Int): String? {
        return getSelectedService(categoryId, serviceId)?.let {
            (it as ComplexSelectedService).specialInstruction
        }
    }

    fun getSelectedService(categoryId: String, serviceId: Int): ISelectedService? {
        val listSelectedValueItem = mapSelectedServices[categoryId]
        return listSelectedValueItem
                ?.first { it.getId() == serviceId }
    }

    fun updateComplexSelectedServiceItem(customiseDisplayData: CustomiseDisplayData) {
        customiseDisplayData.run {
            val listSelectedService = mapSelectedServices[categoryId]
            if (mapSelectedOption.isEmpty()) {
                removeSelectedItem(listSelectedService, serviceItem.getServiceId())
            } else {
                addComplexSelectedService(
                        categoryId,
                        serviceItem,
                        mapSelectedOption,
                        estPrice,
                        specialInstruction)
            }
        }

        updateCountAndPrice()
    }

    private fun addComplexSelectedService(
            categoryId: String,
            service: ComplexCustomisationService,
            mapSelectedOption: HashMap<Int, Int>,
            estPrice: Float,
            specialInstruction: String?) {
        var selectedServices = mapSelectedServices[categoryId]
        if (selectedServices != null) {
            var existed = false
            selectedServices.forEach { selectedService ->
                if (selectedService.getId() == service.getServiceId()) {
                    existed = true
                    (selectedService as ComplexSelectedService).run {
                        this.selectedCustomisation = mapSelectedOption
                        this.subTotal = estPrice
                        this.specialInstruction = specialInstruction
                    }
                }
            }
            if (!existed) {
                selectedServices.add(ComplexSelectedService(
                        service = service,
                        selectedCustomisation = mapSelectedOption,
                        subTotal = estPrice,
                        specialInstruction = specialInstruction)
                )
            }
        } else {
            val selectedService = ComplexSelectedService(
                    service = service,
                    selectedCustomisation = mapSelectedOption,
                    subTotal = estPrice,
                    specialInstruction = specialInstruction)
            selectedServices = mutableListOf(selectedService)
        }
        mapSelectedServices[categoryId] = selectedServices
    }

    /**
     * Create map of <CategoryId, CategoryName>
     */
    fun getMapSelectedCategories(): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        val categories = partnerDetails.value?.categories
        categories?.run {
            for ((cateId, _) in mapSelectedServices) {
                hashMap[cateId] = categories.first { it.id == cateId }.label
            }
        }
        return hashMap
    }
}

interface ISelectedService {
    fun getSelectedCount(): Int
    fun getEstPrice(): Float
    fun getId() : Int
}

data class SelectedService(
        var service: AbsServiceItem,
        var count: Int = 0,
        var subTotal: Float = 0F
): ISelectedService, Serializable {
    override fun getSelectedCount() = count

    override fun getEstPrice() = subTotal

    override fun getId() = service.getServiceId()
}

data class ComplexSelectedService(
        var service: ComplexCustomisationService,
        var selectedCustomisation: HashMap<Int, Int>?, //<Option Index, Selected Position>
        var subTotal: Float,
        var specialInstruction: String?
): ISelectedService, Serializable {
    override fun getSelectedCount() = (selectedCustomisation != null && !selectedCustomisation!!.isEmpty()).toInt()

    override fun getEstPrice() = subTotal

    override fun getId() = service.getServiceId()
}