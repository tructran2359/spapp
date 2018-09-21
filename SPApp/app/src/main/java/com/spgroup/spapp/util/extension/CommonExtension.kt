package com.spgroup.spapp.util.extension

import android.os.Build
import android.text.Html
import com.spgroup.spapp.BuildConfig
import com.spgroup.spapp.util.doLogE
import org.unbescape.html.HtmlEscape

fun Float.formatPrice() = "S$%.2f".format(this)

fun Float.formatPriceWithUnit(unit: String)
        = if(unit.isEmpty()) this.formatPrice() else "${this.formatPrice()} / $unit"

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

fun String.toNoSpecialCharString(): String {
    val noHtml = HtmlEscape.unescapeHtml(this)
    val unicodeIgnoreChar = '\u2028'
    return noHtml.replace(Regex("$unicodeIgnoreChar"), "\r\n")
}

fun Float.toPercentageText() = "$this%"

fun Float.toDiscountText() = "-${this.formatPrice()}"

fun String.toVersionInteger(): Int {
    val list = split(".")
    return if (list.size == 3) {
        try {
            var value = 0
            value += list[0].toInt() * 1000000
            value += list[1].toInt() * 1000
            value += list[2].toInt()
            value
        } catch (ex: Exception) {
            doLogE("Cast", "Can not cast to Int: ${ex.message}")
            -1
        }
    } else {
        doLogE("Cast", "Invalid version format: $this")
        -1
    }
}

fun String.toFloatWithException() = if (isEmpty()) 0f else {
    try {
        this.toFloat()
    } catch (ex: NumberFormatException) {
        ex.printStackTrace()
        doLogE("Cast", "Cast to float: ${ex.message}")
        0f
    }
}