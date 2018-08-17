package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.ComplexCustomisationService

class CustomiseNewViewModel: BaseViewModel() {

    private lateinit var serviceItem: ComplexCustomisationService
    private val selectedMap = mutableMapOf<Int, Int>() //Option index, selected index


    val listData = MutableLiveData<List<CustomiseData>>()
    val totalPriceData = MutableLiveData<Float>()

    fun initData(serviceItem: ComplexCustomisationService) {
        this.serviceItem = serviceItem
        val listCustomiseData = mutableListOf<CustomiseData>()
        serviceItem.customisations.forEach {
            listCustomiseData.add(it.toCustomiseData())
        }
        listData.value = listCustomiseData
    }

    fun notifyDataChanged(optionIndex: Int, selectedPos: Int) {
        selectedMap[optionIndex] = selectedPos
        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        listData.value?.let {customiseData ->
            var totalPrice = 0f
            for ((index, selectedPos) in selectedMap) {
                totalPrice += customiseData[index].options[selectedPos].price
            }

            totalPriceData.value = totalPrice
        }
    }
}

data class CustomiseData(
        val title: String,
        val options: List<CustomiseOption>
)

data class CustomiseOption(
        val label: String,
        val price: Float
)