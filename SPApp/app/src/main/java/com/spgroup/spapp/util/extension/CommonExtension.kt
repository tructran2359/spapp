package com.spgroup.spapp.util.extension

fun Float.formatPrice() = "S$%.2f".format(this)

fun Float.formatPriceWithUnit(unit: String) = "S$${this.formatPrice()} $unit"

fun Float.formatEstPrice() = "EST: ${this.formatPrice()}"