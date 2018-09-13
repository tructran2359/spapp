package com.spgroup.spapp.di.module

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
object UsecaseModule {

    @JvmStatic
    @Provides
    fun provideGetInitialDataUsecase(schedulerFacade: SchedulerFacade, servicesRepository: ServicesRepository): GetInitialDataUsecase {
        return GetInitialDataUsecase(schedulerFacade, servicesRepository)
    }

    @JvmStatic
    @Provides
    fun provideGetPartnerListingUsecase(schedulerFacade: SchedulerFacade, servicesRepository: ServicesRepository)
            : GetPartnerListingUsecase {
        return GetPartnerListingUsecase(schedulerFacade, servicesRepository)
    }

    @JvmStatic
    @Provides
    fun providePreProcessPartnerUsecase(): PreProcessPartnerUsecase {
        return PreProcessPartnerUsecase()
    }

    @JvmStatic
    @Provides
    fun provideGetServicesListByPartnerUsecase(schedulerFacade: SchedulerFacade, servicesRepository: ServicesRepository)
            : GetServicesListByPartnerUsecase {
        return GetServicesListByPartnerUsecase(schedulerFacade, servicesRepository)
    }

    @JvmStatic
    @Provides
    fun provideGetCustomisationLowestPrice(): GetCustomisationLowestPrice {
        return GetCustomisationLowestPrice()
    }
}