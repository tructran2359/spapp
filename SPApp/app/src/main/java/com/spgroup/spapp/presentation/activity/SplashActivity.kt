package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.SplashViewModel
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.appInstance
import com.spgroup.spapp.util.extension.isOnline
import com.spgroup.spapp.util.extension.obtainViewModel
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    private lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        appInstance.appComponent.inject(this)
        splashViewModel = obtainViewModel(SplashViewModel::class.java, vmFactory)

        subscribeUI()

        if (isOnline()) {
            splashViewModel.getInitialData()
        } else {
            startActivityForResult(NoInternetActivity.getLaunchIntentForSplash(this), RC_NO_INTERNET_FOR_SPLASH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_NO_INTERNET_FOR_SPLASH) {
            if (resultCode == Activity.RESULT_OK) {
                splashViewModel.getInitialData()
            } else {
                finish()
            }
        }
    }

    private fun subscribeUI() {
        splashViewModel.isSuccess.observe(this, Observer { isSuccess ->
            isSuccess?.let {
                if (isSuccess) onDownloadDataSuccess()

                // Simulate api call error
//                onDownloadDataFailed(Throwable("Test error Splash"))
            }
        })

        splashViewModel.error.observe(this, Observer {
            onDownloadDataFailed(it)
        })
    }

    private fun onDownloadDataSuccess() {
        val intent = splashViewModel.getIntentToNextActivity(this)
        startActivity(intent)
        finish()
    }

    private fun onDownloadDataFailed(throwable: Throwable?) {
        doLogE("Error", "Error $throwable with message: ${throwable?.message ?: "null"}")
        startActivity(ApiErrorActivity.getLaunchIntentForSplash(this))
        finish()
    }
}