package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.MultiplierService
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import com.spgroup.spapp.presentation.viewmodel.ComplexSelectedService
import com.spgroup.spapp.presentation.viewmodel.ISelectedService
import com.spgroup.spapp.presentation.viewmodel.SelectedService
import com.spgroup.spapp.util.doLogD

class SelectedServiceUsecase: SynchronousUsecase() {

    var mapSelectedServices = HashMap<String, MutableList<ISelectedService>>()

    fun updateNormalSelectedServiceItem(
            absServiceItem: AbsServiceItem,
            count: Int,
            categoryId: String
    ) {
        doLogD(msg = "count:$count catId:$categoryId serId:${absServiceItem.getServiceId()}")

        if (count == 0) {
            removeSelectedItem(categoryId, absServiceItem.getServiceId())
        } else {
            addNormalSelectedItem(absServiceItem, count, categoryId)
        }
    }

    private fun addNormalSelectedItem(
            absServiceItem: AbsServiceItem,
            count: Int,
            categoryId: String) {
        var calculatingList =  mapSelectedServices[categoryId]
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

    fun removeSelectedItem(categoryId: String, serviceId: Int) {
        val listSelectedService = mapSelectedServices[categoryId]
        var deletedPos = -1
        listSelectedService?.forEachIndexed { index, item ->
            if (item.getId() == serviceId) {
                deletedPos = index
                return@forEachIndexed
            }
        }
        if (deletedPos != -1) {
            listSelectedService?.removeAt(deletedPos)
        }
    }

    fun updateComplexSelectedServiceItem(customiseDisplayData: CustomiseDisplayData) {
        customiseDisplayData.run {

            if (mapSelectedOption.isEmpty()) {
                removeSelectedItem(categoryId, serviceItem.getServiceId())
            } else {
                addComplexSelectedService(
                        categoryId,
                        serviceItem,
                        mapSelectedOption,
                        estPrice,
                        specialInstruction)
            }
        }
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

    fun calculateTotalCount(): Int {
        return if (mapSelectedServices.isEmpty()) {
            0
        } else {
            mapSelectedServices
                    .map { it.value } // to list map of List<ISelectedValueItem>
                    .flatten() // to list of all List<ISelectedValueItem>
                    .map { it.getSelectedCount() } // to list of SelectedValueItem#count
                    .sum()
        } // sum of SelectedValueItem#count
    }

    fun calculateEstPrice(): Float {
        return if (mapSelectedServices.isEmpty()) {
            0f
        } else {
            mapSelectedServices
                    .map { it.value }
                    .flatten()
                    .map { it.getEstPrice() }
                    .sum()
        }
    }

    fun getSelectedService(categoryId: String, serviceId: Int): ISelectedService? {
        val listSelectedValueItem = mapSelectedServices[categoryId]
        return listSelectedValueItem
                ?.firstOrNull { it.getId() == serviceId }
    }

    fun getSelectedInstruction(categoryId: String, serviceId: Int): String? {
        return getSelectedService(categoryId, serviceId)?.let {
            (it as ComplexSelectedService).specialInstruction
        }
    }

    fun getListSelectedService(categoryId: String) = mapSelectedServices[categoryId]

    fun removeCategory(categoryId: String) {
        mapSelectedServices.remove(categoryId)
    }

    fun clearAllSelectedServices() {
        mapSelectedServices.clear()
    }

    fun hasCheckboxSelected(): Boolean {
        if (mapSelectedServices.isEmpty()) {
            return false
        }

        for ((_, listSelectedServices) in mapSelectedServices) {
            listSelectedServices.forEach { selectedService ->
                if (selectedService is SelectedService && selectedService.service is CheckboxService) {
                    return true
                }
            }
        }

        return false
    }
}