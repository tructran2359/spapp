package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.domain.model.TopLevelPromotion
import com.spgroup.spapp.domain.model.TopLevelVariable
import com.spgroup.spapp.domain.usecase.RandomiseListFeaturedMerchantUsecase
import com.spgroup.spapp.domain.usecase.RandomiseListFeaturedPromotionUsecase
import com.spgroup.spapp.manager.AppDataCache
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val appDataCache: AppDataCache,
        private val randomisePromoUsecase: RandomiseListFeaturedPromotionUsecase,
        private val randomiseMerchantUsecase: RandomiseListFeaturedMerchantUsecase
) : BaseViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val listTopLevelCate = MutableLiveData<List<TopLevelCategory>>()
    val listTopLevelPromotion = MutableLiveData<List<TopLevelPromotion>>()
    val listTopLevelPartner = MutableLiveData<List<TopLevelFeaturedPartner>>()
    val topLevelVariabes = MutableLiveData<TopLevelVariable>()

    fun getInitData() {
        listTopLevelCate.value = appDataCache.getTopLevelCategories()
        listTopLevelPromotion.value = appDataCache.getTopLevelPromotions()
        listTopLevelPartner.value = appDataCache.getTopLevelPartners()
        topLevelVariabes.value = appDataCache.getTopLevelVariables()
    }

    fun getCategoryByIndex(index: Int) = listTopLevelCate.value?.get(index)

    fun randomiseData() {
        listTopLevelPromotion.value = randomisePromoUsecase.getRandomisedList(listTopLevelPromotion.value)
        listTopLevelPartner.value = randomiseMerchantUsecase.getRandomisedList(listTopLevelPartner.value)
    }

}