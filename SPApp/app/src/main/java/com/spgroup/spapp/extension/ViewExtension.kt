package com.spgroup.spapp.extension

import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
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
    setTextColor(context.resources.getColor(R.color.color_ui02))
}

fun TextView.setUpMenuInactive() {
    setTextColor(context.resources.getColor(R.color.color_ui03))
}