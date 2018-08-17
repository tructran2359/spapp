package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.TopLevelPage
import com.spgroup.spapp.repository.entity.TopLevelPageEntity

class TopLevelPageMapper(val sectionMapper: TopLevelPageSectionMapper): IMapper<TopLevelPageEntity, TopLevelPage> {
    override fun transform(entity: TopLevelPageEntity): TopLevelPage {
        return TopLevelPage(title = entity.title, code = entity.code, sections = sectionMapper.transform(entity.sections))
    }
}