package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.di.Injection
import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.GetCustomisationLowestPrice
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import com.spgroup.spapp.domain.usecase.GetPartnerListingUsecase
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase
import com.spgroup.spapp.manager.AppDataCache

class ViewModelFactory private constructor(
        private val schedulerFacade: SchedulerFacade,
        private val mockRepository: ServicesRepository,
        private val cloudRepository: ServicesRepository,
        private val appDataCache: AppDataCache
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> createSplashVM()

            modelClass.isAssignableFrom(PartnerDetailsViewModel::class.java) -> createPartnerDetailsViewModel()

            modelClass.isAssignableFrom(PartnerListingViewModel::class.java) -> createPartnerListingViewModel()

            modelClass.isAssignableFrom(CustomiseViewModel::class.java) -> createCustomiseViewModel()

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> createHomeViewModel()

            modelClass.isAssignableFrom(OrderSummaryViewModel::class.java) -> createOrderSummaryViewModel()

            modelClass.isAssignableFrom(PageViewModel::class.java) -> createPageViewModel()

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }

    private fun createPageViewModel(): PageViewModel {
        return PageViewModel(appDataCache)
    }

    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // ViewModel creation

    private fun createSplashVM(): SplashViewModel {
        val getInitialDataUsecase = GetInitialDataUsecase(schedulerFacade, cloudRepository)
        return SplashViewModel(getInitialDataUsecase, appDataCache)
    }

    private fun createPartnerDetailsViewModel(): PartnerDetailsViewModel {
        val getServicesUsecase = GetServicesListByPartnerUsecase(schedulerFacade, cloudRepository)
        val getCustomisationLowestPrice = GetCustomisationLowestPrice()
        return PartnerDetailsViewModel(getServicesUsecase, getCustomisationLowestPrice)
    }

    private fun createPartnerListingViewModel(): PartnerListingViewModel {
        val getPartnerListingUsecase = GetPartnerListingUsecase(schedulerFacade, cloudRepository)
        return PartnerListingViewModel(getPartnerListingUsecase)
    }

    private fun createCustomiseViewModel(): CustomiseViewModel {
        return CustomiseViewModel()
    }

    private fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(appDataCache)
    }

    private fun createOrderSummaryViewModel(): OrderSummaryViewModel {
        return OrderSummaryViewModel(Injection.provideGetOrderSummaryUsecase())
    }

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelFactory(
                        Injection.provideSchedulerFacade(),
                        Injection.provideMockRepository(),
                        Injection.provideCloudRepository(),
                        Injection.provideAppDataCache()
                )
            }
            return INSTANCE
        }
    }
}