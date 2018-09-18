package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import javax.inject.Inject

class CustomiseNewViewModel @Inject constructor(): BaseViewModel() {

    private lateinit var mDisplayData: CustomiseDisplayData
    private var mIsEdit = false
    private lateinit var mOriginalDisplayData: CustomiseDisplayData

    val listData = MutableLiveData<List<CustomiseData>>()
    val totalPriceData = MutableLiveData<Float>()
    val serviceName = MutableLiveData<String>()
    val serviceDescription = MutableLiveData<String>()
    val isDataChanged = MutableLiveData<Boolean>()
    val isUpdateSelectedService = MutableLiveData<Boolean>()

    fun initData(isEdit: Boolean, displayData: CustomiseDisplayData) {
        mIsEdit = isEdit
        mDisplayData = displayData
        mOriginalDisplayData = mDisplayData.clone()

        val listCustomiseData = mutableListOf<CustomiseData>()
        mDisplayData.serviceItem.customisations.forEach {
            listCustomiseData.add(it.toCustomiseData())
        }
        listData.value = listCustomiseData
        val subCateName = mDisplayData.subCateName
        val oriServiceName = mDisplayData.serviceItem.label
        serviceName.value = if (subCateName.isEmpty()) {
            oriServiceName
        } else {
            "$subCateName - $oriServiceName"
        }
        serviceDescription.value = mDisplayData.serviceItem.serviceDescription

        // If not edit from Order Summary -> add new service from Partner Detail
        // Check if it is new selection or user updated selected service
        // in order to set bottom button text
        if (!mIsEdit) {
            isUpdateSelectedService.value = mDisplayData.mapSelectedOption.size != 0
        }
    }

    fun notifyDataChanged(optionIndex: Int, selectedPos: Int) {
        mDisplayData.mapSelectedOption[optionIndex] = selectedPos
        calculateTotalPrice()
        checkIfDataChanged()
    }

    fun notifyInstructionChanged(newInstruction: String) {
        mDisplayData.specialInstruction = newInstruction
        checkIfDataChanged()
    }

    private fun checkIfDataChanged() {
        isDataChanged.value = !mDisplayData.isSameSelectedOptionData(mOriginalDisplayData)
    }

    private fun calculateTotalPrice() {
        listData.value?.let {customiseData ->
            var totalPrice = 0f
            for ((index, selectedPos) in mDisplayData.mapSelectedOption) {
                totalPrice += customiseData[index].options[selectedPos].price
            }

            mDisplayData.estPrice = totalPrice
            totalPriceData.value = totalPrice
        }
    }

    fun getSelectedOptionMap() = mDisplayData.mapSelectedOption

    fun getServiceId() = mDisplayData.serviceItem.getServiceId()

    fun getCategoryId() = mDisplayData.categoryId

    fun getTopLevelCateId() = mDisplayData.topLevelCateId

    fun getDisplayData() = mDisplayData
}

data class CustomiseData(
        val title: String,
        val options: List<CustomiseOption>
)

data class CustomiseOption(
        val label: String,
        val price: Float
)