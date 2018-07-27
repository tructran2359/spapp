package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.ServiceItemCombo

class CustomiseViewModel: ViewModel() {

    val isUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val paxCount: MutableLiveData<Int> = MutableLiveData()
    val riceCount: MutableLiveData<Int> = MutableLiveData()
    val estimatedPrice: MutableLiveData<Float> = MutableLiveData()

    val mInitData = Content(paxCount = 1, riceCount = 1, instruction = "No beef and peanut. Low salt.")
    var mCurrentInstruction = mInitData.instruction
    lateinit var mServiceItem: ServiceItemCombo
    var mIsEdit = false

    init {
        isUpdated.value = false
        paxCount.value = mInitData.paxCount
        riceCount.value = mInitData.riceCount
        estimatedPrice.value = with(mInitData) {
            paxCount * paxPrice + riceCount * ricePrice
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Method
    ///////////////////////////////////////////////////////////////////////////

    fun paxChange(isPlus: Boolean) {
        paxCount.value?.let {
            if (isPlus) paxCount.value = it + 1
            else paxCount.value = it - 1

            calculateEstPrice()
            checkUpdate()
        }
    }

    fun riceChange(isPlus: Boolean) {
        riceCount.value?.let {
            if (isPlus) riceCount.value = it + 1
            else riceCount.value = it - 1

            calculateEstPrice()
            checkUpdate()
        }
    }

    fun instructionChange(instruction: String) {
        mCurrentInstruction = instruction
        checkUpdate()
    }

    private fun checkUpdate() {
        val sameData = paxCount.value!! == mInitData.paxCount
        && riceCount.value!! == mInitData.riceCount
        && mCurrentInstruction.equals(mInitData.instruction)

        if (sameData && isUpdated.value == true) {
            isUpdated.value = false
        } else if (!sameData && isUpdated.value == false) {
            isUpdated.value = true
        }
    }

    private fun calculateEstPrice() {
        var estPrice = 0f
        paxCount.value?.let {
            estPrice += it * mInitData.paxPrice
        }

        riceCount.value?.let {
            estPrice += it * mInitData.ricePrice
        }

        estimatedPrice.value = estPrice
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dummy data class
    ///////////////////////////////////////////////////////////////////////////

    data class Content(
            var paxCount: Int = 0,
            var paxMax: Int = 10,
            var paxMin: Int = 1,
            var paxPrice: Float = 165f,

            var riceCount: Int = 0,
            var riceMax: Int = 10,
            var riceMin: Int = 0,
            var ricePrice: Float = 20f,

            val instruction: String = ""
    )
}