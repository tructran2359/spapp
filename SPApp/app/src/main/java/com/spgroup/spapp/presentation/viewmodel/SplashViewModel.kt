package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import com.spgroup.spapp.manager.AppDataCache
import com.spgroup.spapp.presentation.SPApplication
import com.spgroup.spapp.presentation.activity.HomeActivity
import com.spgroup.spapp.presentation.activity.OnBoardingActivity
import com.spgroup.spapp.presentation.activity.UpdateActivity
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.toVersionInteger

class SplashViewModel(private val getInitialDataUsecase: GetInitialDataUsecase,
                      private val appDataCache: AppDataCache
) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>().apply { value = false }

    fun getInitialData() {
        val disposable = getInitialDataUsecase
                .run()
                .doOnSuccess {
                    appDataCache.saveInitData(
                            it.categories,
                            it.promotions,
                            it.featuredPartners,
                            it.pages,
                            it.variables)
                }
                .subscribe(
                        {
                            isSuccess.value = true

//                            //Simulate api call error
//                            error.value = Throwable("Test Error Splash")
                        },
                        {
                            error.value = it
                        }
                )
        disposeBag.add(disposable)
    }

    fun getAppVersion() = appDataCache.getTopLevelVariables().minVersionAndroid

    fun getIntentToNextActivity(context: Context): Intent {
        val localAppVersionInteger = BuildConfig.VERSION_NAME.toVersionInteger()
        val backendAppVersionInteger = getAppVersion().toVersionInteger()
        val needToUpdate = (localAppVersionInteger != -1 && backendAppVersionInteger != -1 && localAppVersionInteger < backendAppVersionInteger)
        doLogD("Version", "Local: $localAppVersionInteger | Backend: $backendAppVersionInteger | Need: $needToUpdate")
        return if (needToUpdate) {

            UpdateActivity.getLaunchIntent(context)

        } else if (!SPApplication.mAppConfig.isOnBoadingShown()) {

            OnBoardingActivity.getLaunchIntent(context)

        } else {

            HomeActivity.getLaunchIntent(context)

        }
    }
}