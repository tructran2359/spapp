package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.spgroup.spapp.util.extension.isOnline
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Base class for activity
 */
open class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun startActivity(intent: Intent?) {
        if (isOnline()) {
            super.startActivity(intent)
        } else {
            super.startActivity(NoInternetActivity.getLaunchIntent(this))
        }
    }
}