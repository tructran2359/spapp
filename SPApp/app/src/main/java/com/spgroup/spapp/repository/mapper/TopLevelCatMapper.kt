package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.repository.entity.TopLevelCategoryEntity

class TopLevelCatMapper : IMapper<TopLevelCategoryEntity, TopLevelCategory> {

    override fun transform(entity: TopLevelCategoryEntity): TopLevelCategory {
        return entity.run {
            TopLevelCategory(categoryId, name, homeIcon, menuIcon, partnerListingBanner)
        }
    }

}