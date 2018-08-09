package com.spgroup.spapp.repository

import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import com.spgroup.spapp.repository.mapper.HomeDataMapper
import com.spgroup.spapp.repository.mapper.PartnerMapper
import io.reactivex.Single

class ServicesCloudDataStore(
        private val singaporePowerHttpClient: SingaporePowerHttpClient,
        private val homeDataMapper: HomeDataMapper,
        private val partnerMapper: PartnerMapper
) : ServicesRepository {

    override fun getInitialData(): Single<HomeData> =
            singaporePowerHttpClient
                    .getInitialData()
                    .map { homeDataMapper.transform(it) }

    override fun getSuppliersByCategory(categoryId: String): Single<List<Partner>> =
            singaporePowerHttpClient
                    .getPartnersByCategory(categoryId)
                    .map { partnerMapper.transform(it) }

    override fun getSupplierServicesDetails(supplierId: Int): Single<List<ServiceCategory>> {
        // TODO truc
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}