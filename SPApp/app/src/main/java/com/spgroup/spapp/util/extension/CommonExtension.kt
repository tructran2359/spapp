package com.spgroup.spapp.util.extension

import android.os.Build
import android.text.Html
import com.spgroup.spapp.BuildConfig

fun Float.formatPrice() = "S$%.2f".format(this)

fun Float.formatPriceWithUnit(unit: String) = "${this.formatPrice()} / $unit"

fun Float.formatEstPrice() = "EST: ${this.formatPrice()}"

fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhoneNumber() = android.util.Patterns.PHONE.matcher(this).matches()

fun String.isNumberOnly() = this.matches(Regex("[0-9]+"))

fun String.toFullUrl() =
        if (!this.startsWith("http")) {
            BuildConfig.BASE_API + this
        } else {
            this
        }

fun Boolean.toInt() = if (this) 1 else 0

fun Any.getTextOrEmpty(text: String?) = if (text == null) "" else text

fun String.toHtmlUnderlineText() = "<u>$this</u>"

fun String.toHtmlSpanned() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }

fun Float.toPercentageText() = "${this.toInt()}%"

fun Float.toDiscountText() = "-${this.formatPrice()}"
