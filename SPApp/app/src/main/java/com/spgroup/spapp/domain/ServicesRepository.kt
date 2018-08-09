package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.ServiceCategory
import io.reactivex.Single

interface ServicesRepository {

    fun getInitialData(): Single<HomeData>

    fun getSuppliersByCategory(categoryId: String): Single<List<Partner>>

    fun getSupplierServicesDetails(supplierId: Int): Single<List<ServiceCategory>>

}