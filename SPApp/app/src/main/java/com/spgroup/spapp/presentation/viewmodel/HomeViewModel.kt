package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import com.spgroup.spapp.domain.usecase.GetTopLevelCategoryUsecase
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(
    val getTopLevelCategoryUsecase: GetTopLevelCategoryUsecase
): ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private val disposeBag = CompositeDisposable()
    val listTopLevelCate = MutableLiveData<List<TopLevelServiceCategory>>()
    val error = MutableLiveData<Throwable>()

    fun load() {
        val disposable = getTopLevelCategoryUsecase
                .getTopLevelCategory()
                .subscribe(
                        {
                            listTopLevelCate.value = it
                        },
                        {
                            error.value = it
                        }
                )
        disposeBag.addAll(disposable)
    }
}