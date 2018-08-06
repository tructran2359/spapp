package com.spgroup.spapp.repository

import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import com.spgroup.spapp.repository.mapper.PartnerMapper
import com.spgroup.spapp.repository.mapper.TopLevelCatMapper
import io.reactivex.Single

class ServicesCloudDataStore(
        private val singaporePowerHttpClient: SingaporePowerHttpClient,
        private val topLevelCatMapper: TopLevelCatMapper,
        private val partnerMapper: PartnerMapper
) : ServicesRepository {

    override fun getTopLevelServiceCategories(): Single<List<TopLevelCategory>> =
            singaporePowerHttpClient
                    .getTopLevelCategories()
                    .map { topLevelCatMapper.transform(it) }

    override fun getSuppliersByCategory(categoryId: String): Single<List<Partner>> =
            singaporePowerHttpClient
                    .getPartnersByCategory(categoryId)
                    .map { partnerMapper.transform(it) }

    override fun getSupplierServicesDetails(supplierId: Int): Single<List<ServiceCategory>> {
        // TODO truc
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}