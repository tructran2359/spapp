package com.spgroup.spapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.AbsCustomisation
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.manager.AppDataMemCache
import com.spgroup.spapp.presentation.SPApplication
import com.spgroup.spapp.repository.ServicesCloudDataStore
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import com.spgroup.spapp.repository.jsondeserializer.CustomisationJsonDeserializer
import com.spgroup.spapp.repository.jsondeserializer.ServiceItemJsonDeserializer
import com.spgroup.spapp.repository.jsondeserializer.StringJsonDeserializer
import com.spgroup.spapp.repository.mapper.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Injection {

    private val MEM_CACHE_INSTANCE = AppDataMemCache()

    fun provideCloudRepository(): ServicesRepository {
        val topLevelCatMapper = TopLevelCatMapper()
        val topLevelPromoMapper = TopLevelPromotionMapper()
        val toplevelPartnerMapper = TopLevelFeaturedPartnerMapper()
        val topLevlePageMapper = TopLevelPageMapper(TopLevelPageSectionMapper())
        val topLevelVariableMapper = TopLevelVariableMapper()
        val homeDataMapper = HomeDataMapper(
                topLevelCatMapper,
                topLevelPromoMapper,
                toplevelPartnerMapper,
                topLevlePageMapper,
                topLevelVariableMapper)

        val partnerMapper = PartnerMapper()
        val promotionMapper = PromotionMapper()
        val requestAckMapper = RequestAckMapper()
        return ServicesCloudDataStore(provideSingaporePowerHttpClient(), homeDataMapper, partnerMapper, promotionMapper, requestAckMapper)
    }

    fun provideSchedulerFacade(): SchedulerFacade = SchedulerFacade()

    fun provideAppDataCache() = MEM_CACHE_INSTANCE

    private fun provideSingaporePowerHttpClient(): SingaporePowerHttpClient {

        val isDebug = BuildConfig.DEBUG

        //2018-09-01T00:00:00
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(AbsServiceItem::class.java, ServiceItemJsonDeserializer())
                .registerTypeAdapter(AbsCustomisation::class.java, CustomisationJsonDeserializer())
                .registerTypeAdapter(String::class.java, StringJsonDeserializer())
                .create()

        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            println("OkHttpDebug $it")
        })
        loggingInterceptor.level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okhttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(SingaporePowerHttpClient::class.java)
    }

    fun provideGson() = Gson()

    fun provideAppConfigManager() = SPApplication.mAppConfig

}