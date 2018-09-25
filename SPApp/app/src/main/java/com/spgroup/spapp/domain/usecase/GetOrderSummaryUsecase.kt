package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.ContactInfo
import com.spgroup.spapp.domain.model.Order
import com.spgroup.spapp.domain.model.OrderSummary
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.presentation.viewmodel.ISelectedService

class GetOrderSummaryUsecase: SynchronousUsecase() {

    fun getOrderSummaryModel(
            partnerDetails: PartnerDetails,
            mapSelectedServices: HashMap<String, MutableList<ISelectedService>>,
            contactInfo: ContactInfo,
            surcharge: Float,
            overallCostBeforeDiscount: Float,
            overallCostAfterDiscount: Float): OrderSummary {

        val timestamp = System.currentTimeMillis()
        val partnerId = partnerDetails.uen
        val orderDiscountPercentage = partnerDetails.getPercentageDiscountValue()
        val amountDiscount = partnerDetails.getAmountDiscountValue()

        val listOrder = mutableListOf<Order>()
        mapSelectedServices.forEach { (cateId: String, listSelectedServices: MutableList<ISelectedService>) ->
            listSelectedServices.forEach { selectedService: ISelectedService ->
                val serviceType = selectedService.getServiceType()
                val serviceId = selectedService.getId()
                val serviceCateName = partnerDetails.getCategoryById(cateId)?.label ?: ""
                val serviceSubCateName = partnerDetails.getSubCateByCateIdAndServiceId(cateId, serviceId)?.label ?: ""
                val serviceName = selectedService.getServiceName()
                val itemQty = selectedService.getSelectedCount()
                val itemTotalPrice = selectedService.getEstPrice()
                val itemCommentsNotes = selectedService.getSpectialInstructions()
                val itemOptions = selectedService.getSelectedCustomisationList()
                val itemUnit = selectedService.getItemUnit()

                val order = Order(
                        itemCommentsNotes = itemCommentsNotes,
                        itemId = serviceId,
                        itemName = serviceName,
                        itemOptions = itemOptions,
                        itemQty = itemQty,
                        itemTotalPrice = itemTotalPrice,
                        itemUnit = itemUnit,
                        serviceCategoryName = serviceCateName,
                        serviceSubcategoryName = serviceSubCateName,
                        serviceType = serviceType
                )
                listOrder.add(order)
            }
        }

        return OrderSummary(
                timestamp = timestamp,
                partnerId = partnerId,
                orders = listOrder,
                orderDiscountPercentage = orderDiscountPercentage,
                overallCostBeforeDiscount = overallCostBeforeDiscount,
                overallCostAfterDiscount = overallCostAfterDiscount,
                contactInfo = contactInfo,
                surcharge = surcharge,
                amountDiscount = amountDiscount

        )
    }

}