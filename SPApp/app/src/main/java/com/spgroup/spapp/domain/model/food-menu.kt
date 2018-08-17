package com.spgroup.spapp.domain.model

data class FoodMenu(
        val label: String,
        val pdfs: List<Pdf>
)

data class Pdf(
        val title: String,
        val uri: String
)