package com.spgroup.spapp.util.extension

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.spgroup.spapp.R

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

fun Context.getColor(colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.getDimensionPixelSize(resId: Int) = this.resources.getDimensionPixelSize(resId)

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.loadAnimation(resId: Int) = AnimationUtils.loadAnimation(this, resId)