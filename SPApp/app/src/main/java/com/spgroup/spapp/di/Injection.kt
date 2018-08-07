package com.spgroup.spapp.di

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.usecase.GetOrderSummaryUsecase
import com.spgroup.spapp.repository.ServicesDataMock

object Injection {

    fun provideServicesRepository(): ServicesRepository = ServicesDataMock()

    fun provideSchedulerFacade(): SchedulerFacade = SchedulerFacade()

    fun provideGetOrderSummaryUsecase(): GetOrderSummaryUsecase = GetOrderSummaryUsecase()

}