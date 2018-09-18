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
            totalPrice: Float,
            surcharge: Float): OrderSummary {

        val timestamp = System.currentTimeMillis()
        val partnerId = partnerDetails.uen
        val orderDiscountPercentage = partnerDetails.getPercentageDiscountValue()

        val overallCostBeforeDiscount = totalPrice
        val percentageDiscount = totalPrice * orderDiscountPercentage / 100
        val amountDiscount = partnerDetails.getAmountDiscountValue()

        var overallCostAfterDiscount = totalPrice - percentageDiscount - amountDiscount + surcharge

        val listOrder = mutableListOf<Order>()

        mapSelectedServices.forEach { (cateId: String, listSelectedServices: MutableList<ISelectedService>) ->
            listSelectedServices.forEach { selectedService: ISelectedService ->
                val serviceType = selectedService.getServiceType()
                val serviceId = selectedService.getId()
                val serviceCateName = partnerDetails.getCategoryById(cateId)?.label ?: ""
                val serviceSubCateName = partnerDetails.getSubCateByCateIdAndServiceId(cateId, serviceId)?.label ?: ""
                val serviceName = selectedService.getServiceName()
                val optionValue = selectedService.getSelectedCustomisationLabel()
                val itemQty = selectedService.getSelectedCount()
                val itemTotalPrice = selectedService.getEstPrice()
                val itemCommentsNotes = selectedService.getSpectialInstructions()

                val order = Order(
                        serviceType = serviceType,
                        serviceCategoryName = serviceCateName,
                        serviceSubcategoryName = serviceSubCateName,
                        itemId = serviceId,
                        itemName = serviceName,
                        optionValue = optionValue,
                        itemQty = itemQty,
                        itemTotalPrice = itemTotalPrice,
                        itemCommentsNotes = itemCommentsNotes
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