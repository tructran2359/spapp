package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.di.Injection
import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.*
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

            modelClass.isAssignableFrom(CustomiseNewViewModel::class.java) -> createCustomiseNewViewModel()

            modelClass.isAssignableFrom(PartnerInfoViewModel::class.java) -> createPartnerInfoViewModel()

            modelClass.isAssignableFrom(PdfViewModel::class.java) -> createPdfViewModel()

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }

    private fun createPdfViewModel(): PdfViewModel {
        return PdfViewModel()
    }

    private fun createPartnerInfoViewModel(): PartnerInfoViewModel {
        val getServicesUsecase = GetServicesListByPartnerUsecase(schedulerFacade, cloudRepository)
        return PartnerInfoViewModel(getServicesUsecase)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewModel creation
    ///////////////////////////////////////////////////////////////////////////



    private fun createCustomiseNewViewModel(): CustomiseNewViewModel{
        return CustomiseNewViewModel()
    }

    private fun createPageViewModel(): PageViewModel {
        return PageViewModel(appDataCache)
    }

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
        val selectedServiceUsecase = SelectedServiceUsecase()
        val getOrderSummaryUsecase = GetOrderSummaryUsecase()
        val submitRequestUsecase = SubmitRequestUsecase(schedulerFacade, cloudRepository)
        return OrderSummaryViewModel(submitRequestUsecase, selectedServiceUsecase, getOrderSummaryUsecase)
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