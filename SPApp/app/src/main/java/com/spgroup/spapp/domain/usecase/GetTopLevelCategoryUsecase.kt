package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import io.reactivex.Single

class GetTopLevelCategoryUsecase(
        schedulerFacade: SchedulerFacade,
        private val servicesRepository: ServicesRepository
): BaseUsecase(schedulerFacade) {

    fun getTopLevelCategory() : Single<List<TopLevelServiceCategory>> {
        return servicesRepository
                .getTopLevelServiceCategories()
                .subscribeOn(schedulerFacade.workerScheduler())
                .observeOn(schedulerFacade.callbackScheduler())

    }
}