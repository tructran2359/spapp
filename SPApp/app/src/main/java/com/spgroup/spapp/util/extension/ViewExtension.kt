package com.spgroup.spapp.util.extension

import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.activity.BaseActivity
import com.spgroup.spapp.presentation.view.IndicatorTextView
import com.spgroup.spapp.util.GlideApp

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
    GlideApp
            .with(this)
            .load(url)
            .into(this)
}

fun ImageView.loadImageWithDefaultPlaceholder(url: String) {
    GlideApp
            .with(this)
            .load(url)
            .placeholder(R.drawable.bg_rec_rounded_grey)
            .error(R.drawable.bg_rec_rounded_grey)
            .into(this)
}

fun ImageView.loadImageWithPlaceholder(
        url: String,
        placeholderResId: Int,
        errorPlaceHolderResId: Int) {
    GlideApp
            .with(this)
            .load(url)
            .placeholder(placeholderResId)
            .error(errorPlaceHolderResId)
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

fun TextView.setUpClickableUnderlineSpan(
        textWithPlaceHolderResId: Int,
        clickableTextResId: Int,
        action: (() -> Unit)) {
    val clickableText = context.getString(clickableTextResId)
    val formattedText = context.getString(textWithPlaceHolderResId, clickableText)
    val ss = SpannableString(formattedText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(view: View?) {
            action.invoke()
        }

        override fun updateDrawState(ds: TextPaint?) {
            super.updateDrawState(ds)
            ds?.isUnderlineText = true
            ds?.color = context.getColorFromRes(R.color.color_grey)
        }
    }
    val clickableIndex = formattedText.indexOf(clickableText)
    ss.setSpan(clickableSpan, clickableIndex, clickableIndex + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = ss
    movementMethod = LinkMovementMethod.getInstance()
}

fun Animation.setUpAppear(view: View) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            view.isGone = false
        }

        override fun onAnimationStart(p0: Animation?) {
        }
    })
}

fun Animation.setUpDisappear(view: View) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            view.isGone = true
        }

        override fun onAnimationStart(p0: Animation?) {
        }
    })
}

fun View.showWithAnimation(anim: Animation) {
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
        startAnimation(anim)
    }
}

fun View.hideWithAnimation(anim: Animation) {
    if (visibility == View.VISIBLE) {
        startAnimation(anim)
    }
}

fun View.isGoneWithText(string: String?) {
    isGone = string == null || string.isEmpty()
}