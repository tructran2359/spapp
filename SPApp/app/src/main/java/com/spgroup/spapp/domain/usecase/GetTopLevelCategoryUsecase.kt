package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.TopLevelCategory
import io.reactivex.Single

class GetTopLevelCategoryUsecase(
        schedulerFacade: SchedulerFacade,
        private val servicesRepository: ServicesRepository
): BaseUsecase(schedulerFacade) {

    fun getTopLevelCategory() : Single<List<TopLevelCategory>> {
        return servicesRepository
                .getTopLevelServiceCategories()
                .subscribeOn(schedulerFacade.workerScheduler())
                .observeOn(schedulerFacade.callbackScheduler())

    }
}