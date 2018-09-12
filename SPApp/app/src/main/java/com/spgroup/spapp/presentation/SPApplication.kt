package com.spgroup.spapp.presentation

import android.app.Application
import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.spgroup.spapp.BuildConfig

import com.spgroup.spapp.R
import com.spgroup.spapp.di.Injection
import com.spgroup.spapp.manager.AppConfigManager
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class SPApplication: Application() {

    companion object {
        lateinit var mAppInstance: SPApplication
        lateinit var mAppConfig: AppConfigManager
        lateinit var mGoogleAnalytics: GoogleAnalytics
        lateinit var mTracker: Tracker
    }

    override fun onCreate() {
        super.onCreate()
        mAppInstance = this

        val pref = this.getSharedPreferences("SPApp_Pref", Context.MODE_PRIVATE)
        mAppConfig = AppConfigManager(pref, Injection.provideGson())

        mGoogleAnalytics = GoogleAnalytics.getInstance(this)
        mTracker = mGoogleAnalytics.newTracker(BuildConfig.GA_TRACKING_ID)
        initCalligraphy()
    }

    private fun initCalligraphy() {
        //Init Calligraphy
        val defaultFont = getString(R.string.font_default)
        val config = CalligraphyConfig.Builder()
                .setDefaultFontPath(defaultFont)
                .setFontAttrId(R.attr.fontPath)
                .build()
        CalligraphyConfig.initDefault(config)
    }
}