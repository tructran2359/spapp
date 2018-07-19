package com.spgroup.spapp.domain.model

import java.io.Serializable

data class SupplierServiceCategory(val id: Int, val title: String, val services: List<ServiceGroup>): Serializable