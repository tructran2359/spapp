package com.spgroup.spapp.repository.entity

data class TopLevelPageSectionEntity(
        val type: String,
        val title: String?,
        val text: String?,
        val options: List<String>?
)