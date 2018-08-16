package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.ServiceCategory
import io.reactivex.Single

class GetServicesListByPartnerUsecase(schedulerFacade: SchedulerFacade, private val servicesRepository: ServicesRepository)
    : BaseUsecase(schedulerFacade) {

    fun getSupplierServicesList(supplierId: Int): Single<List<ServiceCategory>> {
        return servicesRepository.getPartnerDetailsData(supplierId)
                .subscribeOn(schedulerFacade.workerScheduler())
                .observeOn(schedulerFacade.callbackScheduler())
    }
}