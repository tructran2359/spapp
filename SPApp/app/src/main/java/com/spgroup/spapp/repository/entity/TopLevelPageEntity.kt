package com.spgroup.spapp.repository.entity

data class TopLevelPageEntity(
        val title: String,
        val code: String,
        val sections: List<TopLevelPageSectionEntity>
)