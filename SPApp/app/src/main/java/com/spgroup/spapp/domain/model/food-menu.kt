package com.spgroup.spapp.domain.model

import java.io.Serializable

data class FoodMenu(
        val title: String?,
        val summary: String?,
        val items: List<FoodMenuItem>?
): Serializable

data class FoodMenuItem(
        val label: String?,
        val pdfs: List<Pdf>?
): Serializable {
    fun isValid(): Boolean {
        if (label == null || label.isEmpty()) return false

        if (pdfs == null || pdfs.isEmpty()) return false

        var valid = false
        for (i in 0 until pdfs.size) {
            if (pdfs[i].isValid()) {
                valid = true
                break
            }
        }
        return valid
    }
}

data class Pdf(
        val title: String?,
        val uri: String?
): Serializable {
    fun isValid(): Boolean
            = title != null && !title.isEmpty()
            && uri != null && !uri.isEmpty()
}