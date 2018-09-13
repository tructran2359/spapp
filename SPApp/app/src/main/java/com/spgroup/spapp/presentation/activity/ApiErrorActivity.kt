package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.SplashViewModel
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.appInstance
import com.spgroup.spapp.util.extension.obtainViewModel
import com.spgroup.spapp.util.extension.setLayoutParamsSizeFromDimens
import com.spgroup.spapp.util.extension.updateMainButtonEnable
import kotlinx.android.synthetic.main.activity_error.*
import javax.inject.Inject

class ApiErrorActivity: BaseErrorActivity() {

    companion object {

        fun getLaunchIntent(context: Context) = Intent(context, ApiErrorActivity::class.java)

        fun getLaunchIntentForSplash(context: Context): Intent {
            val intent = Intent(context, ApiErrorActivity::class.java)
            intent.putExtra(EXTRA_FOR_SPLASH, true)
            return intent
        }
    }

    private var mForSplash = false
    private var mViewModel: SplashViewModel? = null

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        mForSplash = intent.getBooleanExtra(EXTRA_FOR_SPLASH, false)

        if (mForSplash) {
            setUpViewModel()
        }
        setUpViews()
    }

    private fun setUpViewModel() {
        mViewModel = obtainViewModel(SplashViewModel::class.java, vmFactory)
        subscibeUI()
    }

    private fun subscibeUI() {
        mViewModel?.run {
            isSuccess.observe(this@ApiErrorActivity, Observer {
                tv_refresh.updateMainButtonEnable(true)
                it?.let { success ->
                    if (success) {
                        val intent = mViewModel?.getIntentToNextActivity(this@ApiErrorActivity)
                        startActivity(intent)
                        finish()
                    }
                }
            })

            error.observe(this@ApiErrorActivity, Observer {
                doLogE("Error", "Error $it with message: ${it?.message}")
                tv_refresh.updateMainButtonEnable(true)
            })
        }
    }

    private fun setUpViews() {
        iv_error_icon.setLayoutParamsSizeFromDimens(
                R.dimen.error_screen_api_error_icon_size_width,
                R.dimen.error_screen_api_error_icon_size_height
        )
        iv_error_icon.setImageResource(R.drawable.error_api)
        tv_title.setText(R.string.api_error_title)
        tv_description.setText(R.string.api_error_desc)
        tv_back.isGone = true

        tv_refresh.setText(if (mForSplash) R.string.retry else R.string.back)
        tv_refresh.setOnClickListener {
            if (mForSplash) {
                tv_refresh.updateMainButtonEnable(false)
                mViewModel?.getInitialData()
            } else {
                onBackPressed()
            }
        }
    }
}