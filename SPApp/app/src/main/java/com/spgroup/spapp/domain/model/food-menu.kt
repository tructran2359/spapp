package com.spgroup.spapp.domain.model

import java.io.Serializable

data class FoodMenu(
        val label: String,
        val pdfs: List<Pdf>
): Serializable

data class Pdf(
        val title: String,
        val uri: String
): Serializable