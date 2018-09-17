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
    fun providePartnerDetailsUsecase(schedulerFacade: SchedulerFacade, servicesRepository: ServicesRepository)
            : GetPartnerDetailsUsecase {
        return GetPartnerDetailsUsecase(schedulerFacade, servicesRepository)
    }

    @JvmStatic
    @Provides
    fun provideGetCustomisationLowestPrice(): GetCustomisationLowestPrice {
        return GetCustomisationLowestPrice()
    }

    @JvmStatic
    @Provides
    fun provideSelectedServiceUsecase(): SelectedServiceUsecase {
        return SelectedServiceUsecase()
    }

    @JvmStatic
    @Provides
    fun provideGetOrderSummaryUsecase(): GetOrderSummaryUsecase {
        return GetOrderSummaryUsecase()
    }

    @JvmStatic
    @Provides
    fun provideSubmitRequestUsecase(schedulerFacade: SchedulerFacade, servicesRepository: ServicesRepository): SubmitRequestUsecase {
        return SubmitRequestUsecase(schedulerFacade, servicesRepository)
    }

    @JvmStatic
    @Provides
    fun provideRandomiseListFeaturedPromotionUsecase(): RandomiseListFeaturedPromotionUsecase {
        return RandomiseListFeaturedPromotionUsecase()
    }

    @JvmStatic
    @Provides
    fun provideRandomiseListFeaturedMerchantUsecase(): RandomiseListFeaturedMerchantUsecase {
        return RandomiseListFeaturedMerchantUsecase()
    }
}