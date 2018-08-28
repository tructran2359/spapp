package com.spgroup.spapp.domain

import com.spgroup.spapp.domain.model.*
import io.reactivex.Single

interface ServicesRepository {

    fun getInitialData(): Single<HomeData>

    fun getPartnersListingData(categoryId: String): Single<PartnersListingData>

    fun getPartnerDetailsData(partnerId: String): Single<PartnerDetails>

    fun submitRequest(orderSummary: OrderSummary): Single<RequestAck>

}