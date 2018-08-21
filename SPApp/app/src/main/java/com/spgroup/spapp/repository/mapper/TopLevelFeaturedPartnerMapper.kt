package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.TopLevelFeaturedPartner
import com.spgroup.spapp.repository.entity.TopLevelFeaturedPartnerEntity

class TopLevelFeaturedPartnerMapper: IMapper<TopLevelFeaturedPartnerEntity, TopLevelFeaturedPartner> {
    override fun transform(entity: TopLevelFeaturedPartnerEntity): TopLevelFeaturedPartner {
        return TopLevelFeaturedPartner(
                id = entity.id,
                name = entity.name,
                category = entity.category,
                logoUrl = entity.logo,
                partnerType = entity.partnerType
        )
    }
}