package com.spgroup.spapp.repository.http

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SingaporePowerHttpClient {

    @GET("api/v1/categories")
    fun getTopLevelCategories(): Single<Any>

    @GET("api/v1/partner/category/{top-level-category-id}")
    fun getPartnersByCategory(@Path("top-level-category-id") categoryId: String): Single<Any>

    @GET("api/v1/partner/detail/{partnerUEN}")
    fun getPartnerDetails(@Path("partnerUEN") partnerUEN: String): Single<Any>
}