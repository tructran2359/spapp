package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.SPApplication
import com.spgroup.spapp.presentation.viewmodel.SplashViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.isOnline
import com.spgroup.spapp.util.extension.toVersionInteger
import org.jetbrains.anko.longToast

class SplashActivity : BaseActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(SplashViewModel::class.java)
        observeViewModel()

        if (isOnline()) {
            splashViewModel.getInitialData()
        } else {
            startActivityForResultWithoutCheckingInternet(NoInternetActivity.getLaunchIntentForSplash(this), RC_NO_INTERNET_FOR_SPLASH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_NO_INTERNET_FOR_SPLASH && resultCode == Activity.RESULT_OK) {
            splashViewModel.getInitialData()
        }
    }

    private fun observeViewModel() {
        splashViewModel.isSuccess.observe(this, Observer { isSuccess ->
            isSuccess?.let {
                if (isSuccess) onDownloadDataSuccess()
            }
        })
        splashViewModel.error.observe(this, Observer { onDownloadDataFailed(it) })
    }

    private fun onDownloadDataSuccess() {
        val localAppVersionInteger = BuildConfig.VERSION_NAME.toVersionInteger()
        val backendAppVersionInteger = splashViewModel.getAppVersion().toVersionInteger()
        val needToUpdate = (localAppVersionInteger != -1 && backendAppVersionInteger != -1 && localAppVersionInteger < backendAppVersionInteger)
        doLogD("Version", "Local: $localAppVersionInteger | Backend: $backendAppVersionInteger | Need: $needToUpdate")
        val intent = if (needToUpdate) {

                                UpdateActivity.getLaunchIntent(this)

                            } else if (!SPApplication.mAppConfig.isOnBoadingShown()) {

                                OnBoardingActivity.getLaunchIntent(this)

                            } else {

                                HomeActivity.getLaunchIntent(this)

                            }

        startActivity(intent)
        finish()
    }

    private fun onDownloadDataFailed(throwable: Throwable?) {
        longToast(throwable?.run { message.toString() } ?: "Unknown error")
    }
}