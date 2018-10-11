package com.spgroup.spapp.di.module

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.google.gson.Gson
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.di.DEP_APP_CONTEXT
import com.spgroup.spapp.di.DEP_APP_GSON
import com.spgroup.spapp.di.DEP_DEBUGGABLE
import com.spgroup.spapp.domain.SchedulerFacade
import com.spgroup.spapp.manager.AppConfigManager
import com.spgroup.spapp.manager.AppDataCache
import com.spgroup.spapp.manager.AppDataMemCache
import com.spgroup.spapp.SPApplication
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule(private val application: SPApplication) {

    @Provides
    @ApplicationScoped
    @Named(DEP_APP_CONTEXT)
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @ApplicationScoped
    @Named(DEP_DEBUGGABLE)
    fun provideDebuggable(): Boolean = BuildConfig.DEBUG

    @Provides
    @ApplicationScoped
    fun provideSchedulerFacade() = SchedulerFacade()

    @Provides
    @ApplicationScoped
    fun provideAppDataCache(): AppDataCache = AppDataMemCache()

    @Provides
    @ApplicationScoped
    fun provideSharedPreference(@Named(DEP_APP_CONTEXT) context: Context): SharedPreferences {
        return context.getSharedPreferences("SPApp_Pref", Context.MODE_PRIVATE)
    }

    @Provides
    @ApplicationScoped
    fun provideAppConfigManager(sharedPreferences: SharedPreferences, @Named(DEP_APP_GSON) gson: Gson): AppConfigManager {
        return AppConfigManager(sharedPreferences, gson)
    }

    @Provides
    @ApplicationScoped
    fun provideGoogleAnalytics(@Named(DEP_APP_CONTEXT) context: Context): GoogleAnalytics {
        return GoogleAnalytics.getInstance(context)
    }

    @Provides
    @ApplicationScoped
    fun provideTracker(googleAnalytics: GoogleAnalytics): Tracker {
        val newTracker = googleAnalytics.newTracker(BuildConfig.GA_TRACKING_ID)
        newTracker.enableExceptionReporting(true)
        return newTracker
    }

}