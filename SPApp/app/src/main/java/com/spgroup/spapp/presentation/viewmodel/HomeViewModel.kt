package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.manager.AppDataCache

class HomeViewModel(
        private val appDataCache: AppDataCache
) : BaseViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val listTopLevelCate = MutableLiveData<List<TopLevelCategory>>()

    fun getTopLevelCategory() {
        listTopLevelCate.value = appDataCache.getTopLevelCategories()
    }

    fun getCategoryByIndex(index: Int) = listTopLevelCate.value?.get(index)

}