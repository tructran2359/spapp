package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.SupplierServiceCategory
import com.spgroup.spapp.domain.usecase.GetServicesListBySupplierUsecase
import io.reactivex.disposables.CompositeDisposable

class SupplierDetailsViewModel(
        private val getServicesListBySupplierUsecase: GetServicesListBySupplierUsecase
) : ViewModel() {

    private val disposeBag = CompositeDisposable()

    val serviceCategories = MutableLiveData<List<SupplierServiceCategory>>()
    val error = MutableLiveData<Throwable>()


    fun loadServices(supplierId: Int) {
        val disposable = getServicesListBySupplierUsecase
                .getSupplierServicesList(supplierId)
                .subscribe(
                        { serviceCategories.value = it },
                        { error.value = it }
                )
        disposeBag.addAll(disposable)
    }


    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }
}