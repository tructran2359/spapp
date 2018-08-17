package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.TopLevelPage
import com.spgroup.spapp.manager.AppDataCache

class PageViewModel(val appDataCache: AppDataCache): BaseViewModel() {

    val page = MutableLiveData<TopLevelPage>()

    fun loadPageFromCache(type: String) {
        val pages = appDataCache.getTopLevelPages()
        pages.forEach {
            if (it.code == type) {
                page.value = it
                return
            }
        }
    }

}