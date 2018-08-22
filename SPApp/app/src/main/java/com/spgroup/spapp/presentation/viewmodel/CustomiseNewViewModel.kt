package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData

class CustomiseNewViewModel: BaseViewModel() {

    private lateinit var mDisplayData: CustomiseDisplayData

    val listData = MutableLiveData<List<CustomiseData>>()
    val totalPriceData = MutableLiveData<Float>()
    val serviceName = MutableLiveData<String>()
    val serviceDescription = MutableLiveData<String>()

    fun initData(displayData: CustomiseDisplayData) {
        mDisplayData = displayData

        val listCustomiseData = mutableListOf<CustomiseData>()
        mDisplayData.serviceItem.customisations.forEach {
            listCustomiseData.add(it.toCustomiseData())
        }
        listData.value = listCustomiseData
        serviceName.value = mDisplayData.serviceItem.label
        serviceDescription.value = mDisplayData.serviceItem.serviceDescription
    }

    fun notifyDataChanged(optionIndex: Int, selectedPos: Int) {
        mDisplayData.mapSelectedOption.set(optionIndex, selectedPos)
        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        listData.value?.let {customiseData ->
            var totalPrice = 0f
            for ((index, selectedPos) in mDisplayData.mapSelectedOption) {
                totalPrice += customiseData[index].options[selectedPos].price
            }

            totalPriceData.value = totalPrice
        }
    }

    fun getSelectedOptionMap() = mDisplayData.mapSelectedOption

    fun getServiceId() = mDisplayData.serviceItem.getServiceId()

    fun getCategoryId() = mDisplayData.categoryId

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