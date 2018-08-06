package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.domain.model.ServiceItemCounter
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.domain.usecase.GetServicesListBySupplierUsecase
import com.spgroup.spapp.util.doLogD
import io.reactivex.disposables.CompositeDisposable

class SupplierDetailsViewModel(
        private val getServicesListBySupplierUsecase: GetServicesListBySupplierUsecase
) : ViewModel() {

    private val disposeBag = CompositeDisposable()

    val serviceCategories = MutableLiveData<List<ServiceCategory>>()
    var selectedServiceCategories: List<ServiceCategory>? = null
    val selectedCount = MutableLiveData<Int>()
    val error = MutableLiveData<Throwable>()

    init {
        selectedCount.value = 0
    }


    fun loadServices(supplierId: Int) {
        val disposable = getServicesListBySupplierUsecase
                .getSupplierServicesList(supplierId)
                .subscribe(
                        { serviceCategories.value = it },
                        { error.value = it }
                )
        disposeBag.addAll(disposable)
    }

    fun updateSelectedServiceCategories(count: Int, categoryId: Int, servicePos: Int, itemPos: Int) {
        var totalCount = 0

        selectedServiceCategories?.forEach {
            if (it.id == categoryId) {
                val item = it.getServiceItem(servicePos, itemPos)
                when(item) {

                    is ServiceItemCounter -> item.count = count

                    is ServiceItemCheckBox -> item.selected = (count == 1)

                }
            }
        }

        selectedServiceCategories?.forEach {
            totalCount += it.getSelectedCount()
        }
        doLogD("Test", "updateSelectedServiceCategories Count: $totalCount")

        selectedCount.value = totalCount

    }


    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }
}