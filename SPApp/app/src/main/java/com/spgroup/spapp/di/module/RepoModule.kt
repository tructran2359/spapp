package com.spgroup.spapp.di.module

import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.repository.ServicesCloudDataStore
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import com.spgroup.spapp.repository.mapper.HomeDataMapper
import com.spgroup.spapp.repository.mapper.PartnerMapper
import com.spgroup.spapp.repository.mapper.PromotionMapper
import com.spgroup.spapp.repository.mapper.RequestAckMapper
import dagger.Module
import dagger.Provides

@Module
object RepoModule {

    @JvmStatic
    @Provides
    @ApplicationScoped
    fun provideServicesRepository(
            spHttpClient: SingaporePowerHttpClient,
            homeDataMapper: HomeDataMapper,
            partnerMapper: PartnerMapper,
            promotionMapper: PromotionMapper,
            requestAckMapper: RequestAckMapper
    ): ServicesRepository {
        return ServicesCloudDataStore(spHttpClient, homeDataMapper, partnerMapper, promotionMapper, requestAckMapper)
    }

}