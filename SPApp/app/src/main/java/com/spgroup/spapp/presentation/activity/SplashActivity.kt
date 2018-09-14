package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.SplashViewModel
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    private lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        testAnim()

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
        startActivityWithAnimation(intent)
    }

    private fun onDownloadDataFailed(throwable: Throwable?) {
        doLogE("Error", "Error $throwable with message: ${throwable?.message ?: "null"}")
        startActivity(ApiErrorActivity.getLaunchIntentForSplash(this))
        finish()
    }

    private fun startActivityWithAnimation(intent: Intent) {
        val disappearAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_icon_disappear)
        disappearAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                iv_main.isGone = true
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        val scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_icon_scale_up)
        scaleUpAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                val iconSizeScaledUp = (getDimensionPixelSize(R.dimen.splash_image_size) * 1.2).toInt()
                iv_main.setLayoutParamsSize(iconSizeScaledUp, iconSizeScaledUp)
                iv_main.startAnimation(disappearAnim)
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        iv_main.startAnimation(scaleUpAnim)
    }



    private fun testAnim() {
        val disappearAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_icon_disappear)
        val scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_icon_scale_up)

        disappearAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                iv_main.startAnimation(scaleUpAnim)
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        scaleUpAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                val iconSizeScaledUp = (getDimensionPixelSize(R.dimen.splash_image_size) * 1.2).toInt()
                iv_main.setLayoutParamsSize(iconSizeScaledUp, iconSizeScaledUp)
                iv_main.startAnimation(disappearAnim)
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        iv_main.startAnimation(scaleUpAnim)
    }
}