package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.TopLevelVariable
import com.spgroup.spapp.repository.entity.TopLevelVariableEntity

class TopLevelVariableMapper: IMapper<TopLevelVariableEntity, TopLevelVariable> {
    override fun transform(entity: TopLevelVariableEntity): TopLevelVariable {
        return TopLevelVariable(
                headerLine1 = entity.headerLine1,
                headerLine2 = entity.headerLine2,
                subHeader = entity.subHeader,
                minVersionAndroid = entity.minVersionAndroid,
                minVersionIos = entity.minVersionIos,
                alert = entity.alert
        )
    }
}