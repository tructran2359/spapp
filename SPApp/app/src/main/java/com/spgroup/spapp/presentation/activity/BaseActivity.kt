package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Base class for activity
 */
open class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}