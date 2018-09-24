package com.spgroup.spapp.domain.model

import java.io.Serializable

sealed class AbsServiceItem: Serializable  {
    abstract fun getServiceId(): Int
    abstract fun getServiceType(): String
    abstract fun getServiceName(): String
    abstract fun getServiceUnit(): String?
    open fun getSelectedCustomisation(
            customisationIndex: Int,
            selectedOptionIndex: Int
    ): OrderOption? = null
}


data class ComplexCustomisationService(
        val id: Int,
        val label: String,
        val serviceDescription: String?,
        var priceText: String?,
        val unit: String?,
        val customisations: List<AbsCustomisation>
) : AbsServiceItem(){
    override fun getServiceId() = id
    override fun getServiceType() = "complex"
    override fun getServiceName() = label
    override fun getSelectedCustomisation(customisationIndex: Int, selectedOptionIndex: Int): OrderOption? {
        return customisations[customisationIndex].getSelectedOption(selectedOptionIndex)
    }

    override fun getServiceUnit(): String? = unit
}


data class CheckboxService(
        val id: Int,
        val label: String,
        val serviceDescription: String?,
        val priceText: String
) : AbsServiceItem() {
    override fun getServiceId() = id
    override fun getServiceType() = "checkbox"
    override fun getServiceName() = label
    override fun getServiceUnit(): String? = ""
}


data class MultiplierService(
        val id: Int,
        val label: String,
        val price: Float,
        val min: Int,
        val max: Int,
        val unit: String?
) : AbsServiceItem() {
    override fun getServiceId() = id
    override fun getServiceType() = "multiplier"
    override fun getServiceName() = label
    override fun getServiceUnit(): String? = unit
}