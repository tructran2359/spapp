package com.spgroup.spapp.manager

import com.spgroup.spapp.domain.model.*

interface AppDataCache {
    fun saveInitData(
            topCategories: List<TopLevelCategory>,
            promotions: List<TopLevelPromotion>,
            partners: List<TopLevelFeaturedPartner>,
            pages: List<TopLevelPage>,
            variables: TopLevelVariable)

    fun saveTopLevelCategories(topCategories: List<TopLevelCategory>)
    fun getTopLevelCategories(): List<TopLevelCategory>

    fun saveTopLevelPromotions(promotions: List<TopLevelPromotion>)
    fun getTopLevelPromotions(): List<TopLevelPromotion>

    fun saveTopLevelPartners(partners: List<TopLevelFeaturedPartner>)
    fun getTopLevelPartners(): List<TopLevelFeaturedPartner>

    fun saveTopLevelPages(partners: List<TopLevelPage>)
    fun getTopLevelPages(): List<TopLevelPage>

    fun saveTopLevelVariables(variables: TopLevelVariable)
    fun getTopLevelVariables(): TopLevelVariable

}