package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.manager.AppDataCache

class HomeViewModel(
        private val appDataCache: AppDataCache
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val listTopLevelCate = MutableLiveData<List<TopLevelCategory>>()
    val error = MutableLiveData<Throwable>()

    fun onInitialIntent(intent: Intent) {
        listTopLevelCate.value = appDataCache.getTopLevelCategories()
    }

    fun getCategoryByIndex(index: Int) = listTopLevelCate.value?.get(index)

}