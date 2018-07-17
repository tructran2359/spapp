package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.Supplier
import com.spgroup.spapp.domain.model.SupplierServiceCategory
import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import io.reactivex.Single

interface ServicesRepository {

    fun getTopLevelServiceCategories(): Single<List<TopLevelServiceCategory>>

    fun getSuppliersByCategory(categoryId: Int): Single<List<Supplier>>

    fun getSupplierServicesDetails(supplierId: Int): Single<List<SupplierServiceCategory>>

}