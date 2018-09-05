package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.repository.entity.PartnerEntity

class PartnerMapper : IMapper<PartnerEntity, Partner> {
    override fun transform(entity: PartnerEntity): Partner {
        return entity.run {
            Partner(
                    uen = id,
                    name = name,
                    imgUrl = logo,
                    priceDescription = priceDescription,
                    highlight = highlight ?: "",
                    partnerType = partnerType
            )
        }
    }
}