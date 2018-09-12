package com.spgroup.spapp.di.module

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import dagger.Module
import dagger.Provides

@Module
object UsecaseModule {

    @JvmStatic
    @Provides
    fun provideGetInitialDataUsecase(schedulerFacade: SchedulerFacade, servicesRepository: ServicesRepository): GetInitialDataUsecase {
        return GetInitialDataUsecase(schedulerFacade, servicesRepository)
    }

}