package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.Promotion
import com.spgroup.spapp.repository.entity.PromotionEntity

class PromotionMapper : IMapper<PromotionEntity, Promotion> {
    override fun transform(entity: PromotionEntity): Promotion {
        return entity.run {
            Promotion(image, promoText)
        }
    }
}