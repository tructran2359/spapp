package com.spgroup.spapp.repository

import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.model.PartnersListingData
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import com.spgroup.spapp.repository.mapper.HomeDataMapper
import com.spgroup.spapp.repository.mapper.PartnerMapper
import com.spgroup.spapp.repository.mapper.PromotionMapper
import io.reactivex.Single

class ServicesCloudDataStore(
        private val singaporePowerHttpClient: SingaporePowerHttpClient,
        private val homeDataMapper: HomeDataMapper,
        private val partnerMapper: PartnerMapper,
        private val promotionMapper: PromotionMapper
) : ServicesRepository {
    override fun getInitialData(): Single<HomeData> =
            singaporePowerHttpClient
                    .getInitialData()
                    .map { homeDataMapper.transform(it) }

    override fun getPartnersListingData(categoryId: String): Single<PartnersListingData> =
            singaporePowerHttpClient
                    .getPartnersByCategory(categoryId)
                    .map { response ->
                        val partners = partnerMapper.transform(response.partners)
                        val promotions = promotionMapper.transform(response.promotions)
                        PartnersListingData(partners, promotions)
                    }

    override fun getPartnerDetailsData(partnerId: String): Single<PartnerDetails> =
            singaporePowerHttpClient
                    .getPartnerDetails(partnerId)
                    .filter { it.isNotEmpty() }
                    .toSingle()
                    .map { it[0] }

}
