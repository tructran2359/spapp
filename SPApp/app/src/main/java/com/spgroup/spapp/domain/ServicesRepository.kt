package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.model.PartnersListingData
import io.reactivex.Single

interface ServicesRepository {

    fun getInitialData(): Single<HomeData>

    fun getPartnersListingData(categoryId: String): Single<PartnersListingData>

    fun getPartnerDetailsData(partnerId: String): Single<PartnerDetails>

}