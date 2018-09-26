package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.appInstance
import com.spgroup.spapp.util.extension.isOnline
import org.jetbrains.anko.longToast
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

/**
 * Base class for activity
 */
open class BaseActivity: AppCompatActivity() {

    companion object {
        const val RC_NO_INTERNET = 1001
        const val RC_NO_INTERNET_FOR_SPLASH = 1002
        const val RC_NO_INTERNET_FOR_SUBMIT_REQUEST = 1003
        const val RC_CUSTOMISE = 1
        const val RC_ORDER_SUMMARY = 2
        const val RC_EMPTY_REQUEST = 3
        const val RC_EDIT = 11
        const val EXTRA_PENDING_INTENT = "EXTRA_PENDING_INTENT"
        const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"
        const val EXTRA_FOR_SPLASH = "EXTRA_FOR_SPLASH"

        const val NO_REQUEST_CODE = -1
    }

    @Inject lateinit var mTracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)
        mTracker.run {
            val screenName = this::class.java.simpleName
//        doLogD("ScreenName", screenName)
            setScreenName(screenName)
            send(HitBuilders.ScreenViewBuilder().build())
        }
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_NO_INTERNET && resultCode == Activity.RESULT_OK) {
            data?.let {
                val pendingRequestCode = it.getIntExtra(EXTRA_REQUEST_CODE, NO_REQUEST_CODE)
                val pendingIntent: Intent? = it.extras?.getParcelable(EXTRA_PENDING_INTENT)
                if (pendingRequestCode == NO_REQUEST_CODE) {
                    startActivity(pendingIntent)
                } else {
                    startActivityForResult(pendingIntent, pendingRequestCode)
                }
            }
        }
    }

    fun startActivityWithCheckingInternet(intent: Intent?) {
        if (isOnline()) {
            super.startActivity(intent)
        } else {
            super.startActivityForResult(NoInternetActivity.getLaunchIntent(this, intent, NO_REQUEST_CODE), RC_NO_INTERNET)
        }
    }

    fun startActivityForResultWithCheckingInternet(intent: Intent?, requestCode: Int) {
        if (isOnline()) {
            super.startActivityForResult(intent, requestCode)
        } else {
            super.startActivityForResult(NoInternetActivity.getLaunchIntent(this, intent, requestCode), RC_NO_INTERNET)
        }
    }

    fun makePhoneCall(phoneNumber: String) {
        if (phoneNumber.isEmpty()) {
            longToast(R.string.error_phone_number_empty)
        } else {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
    }
}