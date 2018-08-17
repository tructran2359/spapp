package com.spgroup.spapp.domain.model

sealed class TopLevelPageSection

data class TopLevelPageSectionLongText (
        val text: String
): TopLevelPageSection()

data class TopLevelPageSectionLink (
        val title: String,
        val email: String
): TopLevelPageSection()

data class TopLevelPageSectionList (
        val title: String,
        val options: List<String>
): TopLevelPageSection()

data class TopLevelPageSectionSubtitle (
        val title: String
): TopLevelPageSection()
