package com.spgroup.spapp.domain.model

data class TopLevelVariable(
        val headerLine1: String,
        val headerLine2: String,
        val subHeader: String,
        val minVersionAndroid: String,
        val minVersionIos: String,
        val alert: String,
        val appLinkAndroid: String,
        val appLinkIos: String
)