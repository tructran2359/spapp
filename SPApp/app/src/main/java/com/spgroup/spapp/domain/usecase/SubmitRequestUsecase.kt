package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.OrderSummary
import com.spgroup.spapp.domain.model.RequestAck
import io.reactivex.Single

class SubmitRequestUsecase(
        schedulerFacade: SchedulerFacade,
        private val servicesRepository: ServicesRepository
): BaseUsecase(schedulerFacade) {

    fun submitRequest(orderSummary: OrderSummary): Single<RequestAck> {
        return servicesRepository.submitRequest(orderSummary)
                .subscribeOn(schedulerFacade.workerScheduler())
                .observeOn(schedulerFacade.callbackScheduler())
    }

}