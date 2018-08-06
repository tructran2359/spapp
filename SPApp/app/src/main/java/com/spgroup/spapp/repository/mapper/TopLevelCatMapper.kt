package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.TopLevelServiceCategory
import com.spgroup.spapp.repository.entity.TopLevelCategoryEntity

class TopLevelCatMapper : IMapper<TopLevelCategoryEntity?, TopLevelServiceCategory> {

    override fun transform(entity: TopLevelCategoryEntity?): TopLevelServiceCategory {
        return entity?.let {
            TopLevelServiceCategory(it.categoryId, it.name)
        } ?: TopLevelServiceCategory("", "")
    }

}