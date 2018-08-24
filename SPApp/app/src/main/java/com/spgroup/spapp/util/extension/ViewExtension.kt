package com.spgroup.spapp.util.extension

import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.activity.BaseActivity
import com.spgroup.spapp.presentation.view.IndicatorTextView

fun View.setOnGlobalLayoutListener(action: () -> Unit) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            action()
            this@setOnGlobalLayoutListener.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun TextView.setUpMenuActive() {
    setTextColor(getColor(R.color.color_ui02))
}

fun TextView.setUpMenuInactive() {
    setTextColor(getColor(R.color.color_ui03))
}

fun View.getColor(colorId: Int) = ContextCompat.getColor(context, colorId)

fun View.getDrawable(drawableId: Int) = ContextCompat.getDrawable(context, drawableId)

fun Context.getColorFromRes(colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.getDimensionPixelSize(resId: Int) = this.resources.getDimensionPixelSize(resId)

fun Context.getDimension(resId: Int) = this.resources.getDimension(resId)

fun View.getDimensionPixelSize(resId: Int) = context.getDimensionPixelSize(resId)

fun Context.loadAnimation(resId: Int) = AnimationUtils.loadAnimation(this, resId)

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun Context.inflate(layoutId: Int) = LayoutInflater.from(this).inflate(layoutId, null,false)

fun BaseActivity.getDisplayMetrics(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

fun TextView.setTextUnderline(resId: Int) {
    val text = "<u> ${context.getString(resId)}</u>"
    val spanned = text.toHtmlSpanned()
    setText(spanned)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.getWindowToken(), 0)
}

fun View.setLayoutParamsHeight(heightInPixel: Int) {
    val layoutParams = this.layoutParams
    layoutParams.height = heightInPixel
    this.layoutParams = layoutParams
}

fun View.setLayoutParamsWidth(widthInPixel: Int) {
    val layoutParams = this.layoutParams
    layoutParams.width = widthInPixel
    this.layoutParams = layoutParams
}

fun View.updateVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String) {
    Glide
            .with(this)
            .load(url)
            .into(this)
}

fun LinearLayout.addIndicatorText(listString: List<String>?) {
    if (listString == null || listString.isEmpty()) {
        isGone = true
    } else {
        listString.forEach { offer ->
            if (!listString.isEmpty()) {
                val indicatorTextView = IndicatorTextView(context, offer)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.topMargin = context.getDimensionPixelSize(R.dimen.common_vert_medium_sub)
                indicatorTextView.layoutParams = layoutParams
                addView(indicatorTextView)
            }
        }
    }
}