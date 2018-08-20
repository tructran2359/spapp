package com.spgroup.spapp.manager

import com.spgroup.spapp.domain.model.*

class AppDataMemCache : AppDataCache {

    private lateinit var topLevelCategories: List<TopLevelCategory>
    private lateinit var topLevelPromotions: List<TopLevelPromotion>
    private lateinit var topLevelPartners: List<TopLevelFeaturedPartner>
    private lateinit var topLevelPages: List<TopLevelPage>
    private lateinit var topLevelVariables: TopLevelVariable

    override fun saveInitData(
            topCategories: List<TopLevelCategory>,
            promotions: List<TopLevelPromotion>,
            partners: List<TopLevelFeaturedPartner>,
            pages: List<TopLevelPage>,
            variables: TopLevelVariable
    ) {
        saveTopLevelCategories(topCategories)
        saveTopLevelPromotions(promotions)
        saveTopLevelPartners(partners)
        saveTopLevelPages(pages)
        saveTopLevelVariables(variables)
    }

    override fun saveTopLevelCategories(categories: List<TopLevelCategory>) {
        topLevelCategories = categories
    }

    override fun getTopLevelCategories(): List<TopLevelCategory> {
        return topLevelCategories
    }

    override fun saveTopLevelPromotions(promotions: List<TopLevelPromotion>) {
        topLevelPromotions = promotions
    }

    override fun getTopLevelPromotions(): List<TopLevelPromotion> {
        return topLevelPromotions
    }

    override fun saveTopLevelPartners(partners: List<TopLevelFeaturedPartner>) {
        topLevelPartners = partners
    }

    override fun getTopLevelPartners(): List<TopLevelFeaturedPartner> {
        return topLevelPartners
    }

    override fun saveTopLevelPages(partners: List<TopLevelPage>) {
        topLevelPages = partners
    }

    override fun getTopLevelPages(): List<TopLevelPage> {
        return topLevelPages
    }

    override fun saveTopLevelVariables(variables: TopLevelVariable) {
        topLevelVariables = variables
    }

    override fun getTopLevelVariables(): TopLevelVariable {
        return topLevelVariables
    }
}