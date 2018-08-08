package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.HomeData
import io.reactivex.Single

class GetInitialDataUsecase(
        schedulerFacade: SchedulerFacade,
        private val servicesRepository: ServicesRepository
) : BaseUsecase(schedulerFacade) {

    fun run(): Single<HomeData> = servicesRepository
            .getInitialData()
            .subscribeOn(schedulerFacade.workerScheduler())
            .observeOn(schedulerFacade.callbackScheduler())
}