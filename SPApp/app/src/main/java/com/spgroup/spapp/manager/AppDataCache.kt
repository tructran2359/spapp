package com.spgroup.spapp.manager

import com.spgroup.spapp.domain.model.TopLevelCategory

interface AppDataCache {
    fun saveTopLevelCategories(topCategories: List<TopLevelCategory>)
    fun getTopLevelCategories(): List<TopLevelCategory>
}