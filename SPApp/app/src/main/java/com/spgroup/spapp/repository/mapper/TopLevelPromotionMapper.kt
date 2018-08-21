package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.TopLevelPromotion
import com.spgroup.spapp.repository.entity.TopLevelPromotionEntity

class TopLevelPromotionMapper: IMapper<TopLevelPromotionEntity, TopLevelPromotion> {
    override fun transform(entity: TopLevelPromotionEntity): TopLevelPromotion {
        return TopLevelPromotion(
                imageUrl = entity.image,
                promoText = entity.promoText,
                partnerName = entity.partnerName,
                partnerType = entity.partnerType,
                partnerId = entity.partnerId,
                start = entity.start,
                end = entity.end)
    }
}