package com.spgroup.spapp.domain.model

data class ContactInfo(
        val name: String,
        val email: String,
        val contactNo: String,
        val address: List<String>,
        val prefContactTime: String?,
        val postalCode: String,
        val notes: String?
)