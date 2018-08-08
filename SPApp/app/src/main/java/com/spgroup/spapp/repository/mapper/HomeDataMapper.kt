package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.HomeData
import com.spgroup.spapp.repository.entity.HomeDataEntity

class HomeDataMapper(private val topLevelCatMapper: TopLevelCatMapper) : IMapper<HomeDataEntity, HomeData> {

    override fun transform(entity: HomeDataEntity): HomeData {
        return entity.run {
            val categories = topLevelCatMapper.transform(entity.categories)
            HomeData(categories)
        }
    }

}