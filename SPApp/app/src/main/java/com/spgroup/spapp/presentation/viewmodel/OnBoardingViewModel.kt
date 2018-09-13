package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.util.doLogD
import javax.inject.Inject

class OnBoardingViewModel @Inject constructor(): BaseViewModel() {

    var mPageAnimationState = MutableLiveData<Pair<Int, Boolean>>() //<PagePosition, isAnimationCompleted>

    private val mMapCompletedPage = mutableMapOf<Int, Boolean>()

    fun notifyPageChanged(selectedPage: Int) {
        doLogD("Page", "notifyPageChanged $selectedPage")
        val completed = mMapCompletedPage[selectedPage] ?: false
        mPageAnimationState.value = Pair(selectedPage, completed)
    }

    fun isAnimationCompleted(pagePosition: Int) = mMapCompletedPage[pagePosition] ?: false

    fun setAnimationCompleted(pagePosition: Int) {
        mMapCompletedPage[pagePosition] = true
    }

}