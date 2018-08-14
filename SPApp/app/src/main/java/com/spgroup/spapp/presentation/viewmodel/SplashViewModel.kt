package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import com.spgroup.spapp.manager.AppDataCache

class SplashViewModel(private val getInitialDataUsecase: GetInitialDataUsecase,
                      private val appDataCache: AppDataCache
) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>().apply { value = false }

    fun getInitialData() {
        val disposable = getInitialDataUsecase
                .run()
                .doOnSuccess { appDataCache.saveTopLevelCategories(it.categories) }
                .subscribe(
                        { isSuccess.value = true },
                        { error.value = it })
        disposeBag.add(disposable)
    }
}