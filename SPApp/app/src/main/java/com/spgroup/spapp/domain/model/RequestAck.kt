package com.spgroup.spapp.domain.model

import java.io.Serializable

data class RequestAck (
        val requestNumber: String,
        val title: String,
        val summary: String
): Serializable