package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.repository.entity.HomeDataEntity

class HomeDataMapper(
        private val topLevelCatMapper: TopLevelCatMapper,
        private val topLevelPromoMapper: TopLevelPromotionMapper,
        private val topLevelPartnerMapper: TopLevelFeaturedPartnerMapper,
        private val topLevelPageMapper: TopLevelPageMapper,
        private val topLevelVariableMapper: TopLevelVariableMapper
) : IMapper<HomeDataEntity, HomeData> {

    override fun transform(entity: HomeDataEntity): HomeData {
        return entity.run {
            val categories = topLevelCatMapper.transform(entity.categories)
            val promotions = topLevelPromoMapper.transform(entity.promotions)
            val partners = topLevelPartnerMapper.transform(entity.featuredPartners)
            val pages = topLevelPageMapper.transform(entity.pages)
            val variables = topLevelVariableMapper.transform(entity.variables)
            HomeData(categories, promotions, partners, pages, variables)
        }
    }

}