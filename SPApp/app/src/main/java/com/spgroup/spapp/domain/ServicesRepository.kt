package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.domain.model.TopLevelCategory
import io.reactivex.Single

interface ServicesRepository {

    fun getTopLevelServiceCategories(): Single<List<TopLevelCategory>>

    fun getSuppliersByCategory(categoryId: String): Single<List<Partner>>

    fun getSupplierServicesDetails(supplierId: Int): Single<List<ServiceCategory>>

}