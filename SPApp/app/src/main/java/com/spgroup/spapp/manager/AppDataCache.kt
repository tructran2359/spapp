package com.spgroup.spapp.manager

import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.domain.model.TopLevelPage
import com.spgroup.spapp.domain.model.TopLevelPromotion

interface AppDataCache {
    fun saveInitData(
            topCategories: List<TopLevelCategory>,
            promotions: List<TopLevelPromotion>,
            partners: List<TopLevelFeaturedPartner>,
            pages: List<TopLevelPage>)

    fun saveTopLevelCategories(topCategories: List<TopLevelCategory>)
    fun getTopLevelCategories(): List<TopLevelCategory>

    fun saveTopLevelPromotions(promotions: List<TopLevelPromotion>)
    fun getTopLevelPromotions(): List<TopLevelPromotion>

    fun saveTopLevelPartners(partners: List<TopLevelFeaturedPartner>)
    fun getTopLevelPartners(): List<TopLevelFeaturedPartner>

    fun saveTopLevelPages(partners: List<TopLevelPage>)
    fun getTopLevelPages(): List<TopLevelPage>

}