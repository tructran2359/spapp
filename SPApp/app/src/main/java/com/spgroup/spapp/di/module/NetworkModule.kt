package com.spgroup.spapp.di.module

import com.google.gson.Gson
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.di.*
import com.spgroup.spapp.repository.http.SingaporePowerHttpClient
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
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
    @Named(DEP_LOGGING_INTERCEPTOR)
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
    fun provideOkHttpClient(
            @Named(DEP_API_KEY_INTERCEPTOR) headerInterceptor: Interceptor?,
            @Named(DEP_LOGGING_INTERCEPTOR) httpLogger: HttpLoggingInterceptor): OkHttpClient {

        val builder =  OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLogger)

        if (headerInterceptor != null) {
            builder.addInterceptor(headerInterceptor)
        }

        return builder.build()
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

    @JvmStatic
    @Provides
    @ApplicationScoped
    @Named(DEP_API_KEY)
    fun provideApiKey() = BuildConfig.API_KEY

    @JvmStatic
    @Provides
    @ApplicationScoped
    @Named(DEP_API_KEY_INTERCEPTOR)
    fun provideHeaderInterceptor(@Named(DEP_API_KEY) apiKey: String): Interceptor? {
        if (apiKey.isEmpty()) return null

        return Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Authorization", apiKey).build()
            chain.proceed(request)
        }
    }
}