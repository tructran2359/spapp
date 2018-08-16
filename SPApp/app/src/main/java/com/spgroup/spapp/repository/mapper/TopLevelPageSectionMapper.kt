package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.*
import com.spgroup.spapp.repository.entity.TopLevelPageSectionEntity
import com.spgroup.spapp.util.extension.getTextOrEmpty

class TopLevelPageSectionMapper: IMapper<TopLevelPageSectionEntity, TopLevelPageSection> {
    override fun transform(entity: TopLevelPageSectionEntity): TopLevelPageSection {
        return when(entity.type) {
            "page_long_text" -> TopLevelPageSectionLongText(getTextOrEmpty(entity.text))
            "page_link" -> TopLevelPageSectionLink(email = getTextOrEmpty(entity.email), title = getTextOrEmpty(entity.title))
            "page_list" -> TopLevelPageSectionList(title = getTextOrEmpty(entity.title), options = if (entity.options != null) entity.options else listOf<String>())
            "page_subtitle" -> TopLevelPageSectionSubtitle(title = getTextOrEmpty(entity.title), text = getTextOrEmpty(entity.text))
            else -> throw IllegalArgumentException("Invalid type: ${entity.type}")
        }
    }

}