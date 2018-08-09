package com.spgroup.spapp.domain.model

import java.io.Serializable

data class TopLevelCategory(
        val id: String,
        val name: String,
        val homeThumbnail: String,
        val menuIcon: String,
        val banner: String
) : Serializable