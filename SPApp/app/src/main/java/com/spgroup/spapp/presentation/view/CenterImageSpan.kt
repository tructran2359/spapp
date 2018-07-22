package com.spgroup.spapp.presentation.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import java.lang.ref.WeakReference

/**
 * Source: https://gist.github.com/rajeefmk/221eea0a0fa168f3f7cc666a52692641
 */
class CenterImageSpan(private var mDrawableRef: WeakReference<Drawable>?): DynamicDrawableSpan() {


    constructor(drawable: Drawable): this(null) {
        mDrawableRef = WeakReference(drawable)
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val d = getCachedDrawable()
        val rect = d.bounds

        if (fm != null) {
            fm!!.ascent = -rect.bottom
            fm!!.descent = 0

            fm!!.top = fm!!.ascent
            fm!!.bottom = 0
        }

        return rect.right
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val b = getCachedDrawable()
        canvas.save()

        var transY: Float = (bottom - b.bounds.bottom / 2).toFloat()
        val fm = paint.getFontMetricsInt()
        transY -= fm.descent - fm.ascent / 2

        canvas.translate(x, transY)
        b.draw(canvas)
        canvas.restore()
    }

    override fun getDrawable(): Drawable {
        return mDrawableRef!!.get()!!
    }

    private fun getCachedDrawable(): Drawable {
        val wr = mDrawableRef
        var d: Drawable? = null

        if (wr != null)
            d = wr!!.get()

        if (d == null) {
            d = drawable
            mDrawableRef = WeakReference(d)
        }

        return d
    }


}