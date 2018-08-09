package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.PartnersListingData
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.domain.usecase.GetPartnerListingUsecase

class PartnerListingViewModel(
        private val getPartnerListingUsecase: GetPartnerListingUsecase
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
                            partnerListingItems.value = mergePartnersWithPromotions(it, 4)
                        },
                        {
                            error.value = it
                        }
                )
        disposeBag.addAll(disposable)
    }

    private fun mergePartnersWithPromotions(data: PartnersListingData, promotionIndex: Int): List<PartnersListingItem> {
        if (data.promotions.isEmpty()) {
            return data.partners
        } else {
            if (data.partners.size < promotionIndex) {
                return data.partners
            } else {
                val mergedList: MutableList<PartnersListingItem> = data.partners.toMutableList()
                if (data.partners.size == promotionIndex) {
                    mergedList.add(data.promotions[0])
                } else {
                    mergedList.add(promotionIndex, data.promotions[0])
                }
                return mergedList
            }
        }
    }
}