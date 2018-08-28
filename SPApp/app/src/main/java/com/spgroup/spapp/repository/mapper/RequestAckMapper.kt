package com.spgroup.spapp.repository.mapper

import com.spgroup.spapp.domain.model.RequestAck
import com.spgroup.spapp.repository.entity.RequestAckEntity

class RequestAckMapper: IMapper<RequestAckEntity, RequestAck> {

    override fun transform(entity: RequestAckEntity): RequestAck {
        return RequestAck(
                entity.requestNumber,
                entity.tyTitle,
                entity.tySummary
        )
    }

}