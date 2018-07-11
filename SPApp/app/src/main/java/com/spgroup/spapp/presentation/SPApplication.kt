package com.spgroup.spapp.presentation

import android.app.Application
import com.spgroup.spapp.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class SPApplication: Application() {

    override fun onCreate() {
        super.onCreate()
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