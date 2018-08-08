package com.spgroup.spapp.repository.http

import com.spgroup.spapp.repository.entity.HomeDataEntity
import com.spgroup.spapp.repository.entity.PartnerEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SingaporePowerHttpClient {

    @GET("api/v1/home")
    fun getInitialData(): Single<HomeDataEntity>

    @GET("api/v1/partner/category/{top-level-category-id}")
    fun getPartnersByCategory(@Path("top-level-category-id") categoryId: String): Single<List<PartnerEntity>>

    @GET("api/v1/partner/detail/{partnerUEN}")
    fun getPartnerDetails(@Path("partnerUEN") partnerUEN: String): Single<Any>
}