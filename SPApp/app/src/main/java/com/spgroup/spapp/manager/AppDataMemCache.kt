package com.spgroup.spapp.manager

import com.spgroup.spapp.domain.model.TopLevelCategory

class AppDataMemCache : AppDataCache {

    private lateinit var topLevelCategories: List<TopLevelCategory>

    override fun saveTopLevelCategories(categories: List<TopLevelCategory>) {
        topLevelCategories = categories
    }

    override fun getTopLevelCategories(): List<TopLevelCategory> {
        return topLevelCategories
    }
}