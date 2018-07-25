package com.spgroup.spapp.presentation.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan

/**
 * Source: https://gist.github.com/rajeefmk/221eea0a0fa168f3f7cc666a52692641
 */
class CenterImageSpan(private var mDrawable: Drawable): DynamicDrawableSpan() {

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val rect = mDrawable.bounds

        if (fm != null) {
            fm.ascent = -rect.bottom
            fm.descent = 0

            fm.top = fm.ascent
            fm.bottom = 0
        }

        return rect.right
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        canvas.save()

        var transY: Float = (bottom - mDrawable.bounds.bottom / 2).toFloat()
        val fm = paint.getFontMetricsInt()
        transY -= fm.descent - fm.ascent / 2

        canvas.translate(x, transY)
        mDrawable.draw(canvas)
        canvas.restore()
    }

    override fun getDrawable(): Drawable {
        return mDrawable
    }

}