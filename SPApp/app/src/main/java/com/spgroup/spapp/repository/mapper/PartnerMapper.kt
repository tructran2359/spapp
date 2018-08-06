package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.Supplier
import com.spgroup.spapp.repository.entity.PartnerEntity

class PartnerMapper : IMapper<PartnerEntity, Supplier> {
    override fun transform(entity: PartnerEntity): Supplier {
        return entity.run {
            Supplier(
                    uen = id,
                    name = name,
                    imgUrl = logo,
                    priceDescription = priceDescription,
                    highlight = highlight
            )
        }
    }
}