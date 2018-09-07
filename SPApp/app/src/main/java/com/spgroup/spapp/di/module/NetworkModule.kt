package com.spgroup.spapp.di.module

import com.google.gson.Gson
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.di.DEP_DEBUGGABLE
import com.spgroup.spapp.di.DEP_NETWORK_GSON
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
object NetworkModule {

    @JvmStatic
    @Provides
    @ApplicationScoped
    fun provideHttpLogger(@Named(DEP_DEBUGGABLE) isDebug: Boolean): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            println("OkHttpDebug $it")
        })
        loggingInterceptor.level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return loggingInterceptor
    }

    @JvmStatic
    @Provides
    @ApplicationScoped
    fun provideOkHttpClient(httpLogger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLogger)
                .build()
    }

    @JvmStatic
    @Provides
    @ApplicationScoped
    fun provideRetrofit(okhttpClient: OkHttpClient, @Named(DEP_NETWORK_GSON) gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @JvmStatic
    @Provides
    @ApplicationScoped
    fun provideSingaporePowerHttpClient(retrofit: Retrofit): SingaporePowerHttpClient {
        return retrofit.create(SingaporePowerHttpClient::class.java)
    }
}