package com.spgroup.spapp.domain.model

data class TopLevelPage(
        val title: String,
        val code: String,
        val sections: List<TopLevelPageSection>
)