package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.domain.usecase.GetPartnerListingUsecase
import com.spgroup.spapp.domain.usecase.PreProcessPartnerUsecase
import javax.inject.Inject

class PartnerListingViewModel @Inject constructor(
        private val getPartnerListingUsecase: GetPartnerListingUsecase,
        private val preProccessPartnerUsecase: PreProcessPartnerUsecase
) : BaseViewModel() {

    val topLevelCategory = MutableLiveData<TopLevelCategory>()
    val partnerListingItems = MutableLiveData<List<PartnersListingItem>>()

    fun setInitialData(topLevelCategory: TopLevelCategory) {
        this.topLevelCategory.value = topLevelCategory
    }

    fun loadPartnerListing() {
        val disposable = getPartnerListingUsecase
                .getPartnerListing(topLevelCategory.value?.id)
                .subscribe(
                        {
                            partnerListingItems.value = preProccessPartnerUsecase.run(it)

                            // To Simulate error case
//                            error.value = Throwable("This is a test")
                        },
                        {
                            error.value = it
                        }
                )
        disposeBag.addAll(disposable)
    }

    fun randomisePartnerOrder() {
        partnerListingItems.value?.run {
            val randomisedList = preProccessPartnerUsecase.randomisePartnerOrder(this)
            partnerListingItems.value = randomisedList
        }
    }
}