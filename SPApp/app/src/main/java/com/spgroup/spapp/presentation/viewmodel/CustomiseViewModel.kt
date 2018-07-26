package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.ServiceItemCombo

class CustomiseViewModel: ViewModel() {

    val isUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val paxCount: MutableLiveData<Int> = MutableLiveData()
    val riceCount: MutableLiveData<Int> = MutableLiveData()
    val instruction: MutableLiveData<String> = MutableLiveData()
    val mInitData = Content(paxCount = 1, riceCount = 1, instruction = "No beef and peanut. Low salt.")
    lateinit var mServiceItem: ServiceItemCombo
    var mIsEdit = false

    init {
        isUpdated.value = false
        paxCount.value = mInitData.paxCount
        riceCount.value = mInitData.riceCount
        instruction.value = mInitData.instruction
    }

    ///////////////////////////////////////////////////////////////////////////
    // Method
    ///////////////////////////////////////////////////////////////////////////

    fun paxChange(isPlus: Boolean) {
        paxCount.value?.let {
            if (isPlus) paxCount.value = it + 1
            else paxCount.value = it - 1

            checkUpdate()
        }
    }

    fun riceChange(isPlus: Boolean) {
        riceCount.value?.let {
            if (isPlus) riceCount.value = it + 1
            else riceCount.value = it - 1

            checkUpdate()
        }
    }


    private fun checkUpdate() {
        val sameData = paxCount.value!! == mInitData.paxCount
        && riceCount.value!! == mInitData.riceCount
        && instruction.value!!.equals(mInitData.instruction)

        if (sameData && isUpdated.value == true) {
            isUpdated.value = false
        } else if (!sameData && isUpdated.value == false) {
            isUpdated.value = true
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dummy data class
    ///////////////////////////////////////////////////////////////////////////

    data class Content(
            var paxCount: Int = 0,
            var paxMax: Int = 10,
            var paxMin: Int = 1,

            var riceCount: Int = 0,
            var riceMax: Int = 10,
            var riceMin: Int = 0,

            val instruction: String = ""
    )
}