package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.domain.SchedulerFactory
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.GetServicesListBySupplierUsecase
import com.spgroup.spapp.repository.ServiceDataMock

class ViewModelFactory private constructor(
        private val schedulerFactory: SchedulerFactory,
        private val servicesRepository: ServicesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SupplierDetailsViewModel::class.java) -> createSupplierDetailsViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }


    private fun createSupplierDetailsViewModel(): SupplierDetailsViewModel {
        val getServicesUsecase = GetServicesListBySupplierUsecase(schedulerFactory, servicesRepository)
        return SupplierDetailsViewModel(getServicesUsecase)
    }


    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelFactory(SchedulerFactory(), ServiceDataMock())
            }
            return INSTANCE
        }
    }
}