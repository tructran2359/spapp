package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.domain.model.PartnersListingData
import com.spgroup.spapp.domain.model.ServiceCategory
import io.reactivex.Single

interface ServicesRepository {

    fun getInitialData(): Single<HomeData>

    fun getPartnersListingData(categoryId: String): Single<PartnersListingData>

    fun getSupplierServicesDetails(supplierId: Int): Single<List<ServiceCategory>>

}