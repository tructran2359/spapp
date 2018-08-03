package com.spgroup.spapp.util.extension

fun Float.formatPrice() = "S$%.2f".format(this)

fun Float.formatPriceWithUnit(unit: String) = "${this.formatPrice()} / $unit"

fun Float.formatEstPrice() = "EST: ${this.formatPrice()}"

fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhoneNumber() = android.util.Patterns.PHONE.matcher(this).matches()

fun String.isNumberOnly() = this.matches(Regex("[0-9]+"))
