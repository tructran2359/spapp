package com.spgroup.spapp.presentation.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.spgroup.spapp.R
import com.spgroup.spapp.util.doLogD
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
        const val RC_REQUEST_CALL_PERMISSION = 4
        const val RC_EDIT = 11
        const val EXTRA_PENDING_INTENT = "EXTRA_PENDING_INTENT"
        const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"
        const val EXTRA_FOR_SPLASH = "EXTRA_FOR_SPLASH"

        const val NO_REQUEST_CODE = -1
    }

    @Inject lateinit var mTracker: Tracker

    private var mPendingPhoneNumber: String? = null

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_REQUEST_CALL_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPendingPhoneNumber?.run {
                    makePhoneCall(this)
                }
            } else {
                longToast(R.string.error_permission_denied)
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
            val granted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            if (granted != PackageManager.PERMISSION_GRANTED) {
                //request permission
                val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                doLogD("PhoneCall", "Should show: $shouldShow")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), RC_REQUEST_CALL_PERMISSION)
                mPendingPhoneNumber = phoneNumber
                return
            }

            mPendingPhoneNumber = null

            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
    }
}