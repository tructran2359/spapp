package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.Supplier
import io.reactivex.Single

class GetPartnerListingUsecase(

        schedulerFacade: SchedulerFacade,

        private val servicesRepository: ServicesRepository

) : BaseUsecase(schedulerFacade) {

    fun getPartnerListing(topLeverServiceCategoryId: Int): Single<List<Supplier>> {
        return servicesRepository.getSuppliersByCategory(topLeverServiceCategoryId)
                .subscribeOn(schedulerFacade.workerScheduler())
                .observeOn(schedulerFacade.callbackScheduler())
    }

}