package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.PartnerDetails
import io.reactivex.Single

class GetServicesListByPartnerUsecase(schedulerFacade: SchedulerFacade, private val servicesRepository: ServicesRepository)
    : BaseUsecase(schedulerFacade) {

    fun getPartnerDetails(partnerUEN: String): Single<PartnerDetails> {
        return servicesRepository
                .getPartnerDetailsData(partnerUEN)
                .subscribeOn(schedulerFacade.workerScheduler())
                .observeOn(schedulerFacade.callbackScheduler())
    }
}