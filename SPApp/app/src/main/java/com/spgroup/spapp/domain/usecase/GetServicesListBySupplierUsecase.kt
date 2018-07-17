package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.SchedulerFactory
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.SupplierServiceCategory
import io.reactivex.Single

class GetServicesListBySupplierUsecase(schedulerFactory: SchedulerFactory, private val servicesRepository: ServicesRepository)
    : BaseUsecase(schedulerFactory) {

    fun getSupplierServicesList(supplierId: Int): Single<List<SupplierServiceCategory>> {
        return servicesRepository.getSupplierServicesDetails(supplierId)
                .subscribeOn(schedulerFactory.workerScheduler())
                .observeOn(schedulerFactory.callbackScheduler())
    }
}