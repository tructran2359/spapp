package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.Supplier
import com.spgroup.spapp.domain.model.SupplierServiceCategory
import com.spgroup.spapp.domain.model.TopLevelCategory
import io.reactivex.Single

interface ServicesRepository {

    fun getTopLevelServiceCategories(): Single<List<TopLevelCategory>>

    fun getSuppliersByCategory(categoryId: String): Single<List<Supplier>>

    fun getSupplierServicesDetails(supplierId: Int): Single<List<SupplierServiceCategory>>

}