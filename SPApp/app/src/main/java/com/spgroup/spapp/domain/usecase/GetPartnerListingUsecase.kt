package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.PartnersListingData
import io.reactivex.Single

class GetPartnerListingUsecase(

        schedulerFacade: SchedulerFacade,

        private val servicesRepository: ServicesRepository

) : BaseUsecase(schedulerFacade) {

    fun getPartnerListing(topLeverServiceCategoryId: String?): Single<PartnersListingData> {
        topLeverServiceCategoryId?.let {
            return servicesRepository.getPartnersListingData(topLeverServiceCategoryId)
                    .subscribeOn(schedulerFacade.workerScheduler())
                    .observeOn(schedulerFacade.callbackScheduler())
        } ?: throw IllegalArgumentException("category must be provide")

    }

}