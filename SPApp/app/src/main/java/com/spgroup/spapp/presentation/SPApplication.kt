package com.spgroup.spapp.presentation

import android.app.Application
import com.spgroup.spapp.R
import com.spgroup.spapp.manager.AppConfigManager
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class SPApplication: Application() {

    companion object {
        lateinit var mAppInstance: SPApplication
        lateinit var mAppConfig: AppConfigManager
    }

    override fun onCreate() {
        super.onCreate()
        mAppInstance = this
        mAppConfig = AppConfigManager(this)
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