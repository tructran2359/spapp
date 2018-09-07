package com.spgroup.spapp.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spgroup.spapp.di.DEP_APP_GSON
import com.spgroup.spapp.di.DEP_NETWORK_GSON
import com.spgroup.spapp.domain.model.AbsCustomisation
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.repository.jsondeserializer.CustomisationJsonDeserializer
import com.spgroup.spapp.repository.jsondeserializer.ServiceItemJsonDeserializer
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object GsonModule {

    @JvmStatic
    @Provides
    @Named(DEP_NETWORK_GSON)
    fun provideNetworkGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") //2018-09-01T00:00:00
                .registerTypeAdapter(AbsServiceItem::class.java, ServiceItemJsonDeserializer())
                .registerTypeAdapter(AbsCustomisation::class.java, CustomisationJsonDeserializer())
                .create()
    }

    @JvmStatic
    @Provides
    @Named(DEP_APP_GSON)
    fun provideAppGson(): Gson = Gson()
}