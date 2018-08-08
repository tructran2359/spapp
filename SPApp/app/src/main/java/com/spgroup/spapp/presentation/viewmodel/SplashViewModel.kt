package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import com.spgroup.spapp.manager.AppDataCache
import io.reactivex.disposables.CompositeDisposable

class SplashViewModel(private val getInitialDataUsecase: GetInitialDataUsecase,
                      private val appDataCache: AppDataCache
) : ViewModel() {

    private val disposeBag = CompositeDisposable()

    val isSuccess = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<Throwable>()

    fun getInitialData() {
        val disposable = getInitialDataUsecase
                .run()
                .doOnSuccess { appDataCache.saveTopLevelCategories(it.categories) }
                .subscribe(
                        { isSuccess.value = true },
                        { error.value = it })
        disposeBag.add(disposable)
    }

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }
}