package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.di.Injection
import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.GetPartnerListingUsecase
import com.spgroup.spapp.domain.usecase.GetServicesListBySupplierUsecase

class ViewModelFactory private constructor(
        private val schedulerFacade: SchedulerFacade,
        private val servicesRepository: ServicesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(SupplierDetailsViewModel::class.java) -> createSupplierDetailsViewModel()

            modelClass.isAssignableFrom(PartnerListingViewModel::class.java) -> createPartnerListingViewModel()

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }


    private fun createSupplierDetailsViewModel(): SupplierDetailsViewModel {
        val getServicesUsecase = GetServicesListBySupplierUsecase(schedulerFacade, servicesRepository)
        return SupplierDetailsViewModel(getServicesUsecase)
    }

    private fun createPartnerListingViewModel(): PartnerListingViewModel {
        val getPartnerListingUsecase = GetPartnerListingUsecase(schedulerFacade, servicesRepository)
        return PartnerListingViewModel(getPartnerListingUsecase)
    }


    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelFactory(Injection.provideSchedulerFacade(), Injection.provideServicesRepository())
            }
            return INSTANCE
        }
    }
}