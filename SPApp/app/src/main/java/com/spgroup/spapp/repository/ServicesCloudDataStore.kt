package com.spgroup.spapp.repository

import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.Supplier
import com.spgroup.spapp.domain.model.SupplierServiceCategory
import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import com.spgroup.spapp.repository.mapper.TopLevelCatMapper
import io.reactivex.Single

class ServicesCloudDataStore(
        private val singaporePowerHttpClient: SingaporePowerHttpClient,
        private val topLevelCatMapper: TopLevelCatMapper
) : ServicesRepository {

    override fun getTopLevelServiceCategories(): Single<List<TopLevelServiceCategory>> =
            singaporePowerHttpClient
                    .getTopLevelCategories()
                    .map { topLevelCatMapper.transform(it) }

    override fun getSuppliersByCategory(categoryId: Int): Single<List<Supplier>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSupplierServicesDetails(supplierId: Int): Single<List<SupplierServiceCategory>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}