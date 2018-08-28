package com.spgroup.spapp.domain.model

import java.io.Serializable

data class FoodMenu(
        val title: String?,
        val summary: String?,
        val items: List<FoodMenuItem>?
): Serializable

data class FoodMenuItem(
        val label: String,
        val pdfs: List<Pdf>
): Serializable

data class Pdf(
        val title: String,
        val uri: String
): Serializable