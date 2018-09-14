package com.spgroup.spapp.presentation

import android.support.multidex.MultiDexApplication
import com.spgroup.spapp.R
import com.spgroup.spapp.di.component.AppComponent
import com.spgroup.spapp.di.component.DaggerAppComponent
import com.spgroup.spapp.di.module.AppModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class SPApplication: MultiDexApplication() {

    companion object {
        lateinit var mAppInstance: SPApplication
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)

        mAppInstance = this

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