package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import com.spgroup.spapp.domain.usecase.RandomiseListFeaturedMerchantUsecase
import com.spgroup.spapp.domain.usecase.RandomiseListFeaturedPromotionUsecase
import com.spgroup.spapp.manager.AppConfigManager
import com.spgroup.spapp.manager.AppDataCache
import com.spgroup.spapp.presentation.activity.HomeActivity
import com.spgroup.spapp.presentation.activity.OnBoardingActivity
import com.spgroup.spapp.presentation.activity.UpdateActivity
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.toVersionInteger
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val getInitialDataUsecase: GetInitialDataUsecase,
        private val appDataCache: AppDataCache,
        private val mAppConfig: AppConfigManager,
        private val randomisePromoUsecase: RandomiseListFeaturedPromotionUsecase,
        private val randomiseMerchantUsecase: RandomiseListFeaturedMerchantUsecase
) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>().apply { value = false }

    fun getInitialData() {
        val disposable = getInitialDataUsecase
                .run()
                .doOnSuccess {
                    val randomisedPromotionList = randomisePromoUsecase.getRandomisedList(it.promotions)
                    val randomisedPartnerList = randomiseMerchantUsecase.getRandomisedList(it.featuredPartners)
                    appDataCache.saveInitData(
                            it.categories,
                            randomisedPromotionList,
                            randomisedPartnerList,
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

//        // To skip check update
//        val needToUpdate = false

        val needToUpdate = (localAppVersionInteger != -1 && backendAppVersionInteger != -1 && localAppVersionInteger < backendAppVersionInteger)

        doLogD("Version", "Local: $localAppVersionInteger | Backend: $backendAppVersionInteger | Need: $needToUpdate")
        return if (needToUpdate) {

            UpdateActivity.getLaunchIntent(context)

        } else if (!mAppConfig.isOnBoadingShown()) {

            OnBoardingActivity.getLaunchIntent(context)

        } else {

            HomeActivity.getLaunchIntent(context)

        }
    }
}