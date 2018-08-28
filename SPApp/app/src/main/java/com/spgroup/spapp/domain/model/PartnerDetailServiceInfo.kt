package com.spgroup.spapp.domain.model

import java.io.Serializable

data class PartnerDetailServiceInfo(
        val title: String,
        val description: String,
        val list: List<String>
): Serializable