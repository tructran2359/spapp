package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.SplashViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import org.jetbrains.anko.longToast

class SplashActivity : BaseActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(SplashViewModel::class.java)
        observeViewModel()
        splashViewModel.getInitialData()
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
        startActivity(HomeActivity.getLaunchIntent(this))
        finish()
    }

    private fun onDownloadDataFailed(throwable: Throwable?) {
        longToast(throwable?.run { message.toString() } ?: "Unknown error")
    }
}