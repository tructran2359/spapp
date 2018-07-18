package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_simple_action_bar.view.*

class SimpleActionBar: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mTitle: String

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initProperties(context, attributeSet)
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle) {
        initProperties(context, attributeSet)
        initViews(context)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initProperties(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.SimpleActionBar, 0, 0)
        try {
            mTitle = typedArray.getString(R.styleable.SimpleActionBar_title)
        } finally {
            typedArray.recycle()
        }

    }

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_simple_action_bar, this, true)

        tv_title.setText(mTitle)
    }

    fun setOnBackPress(action: () -> Unit) {
        iv_back.setOnClickListener {
            action()
        }
    }

    fun setTitle(resId: Int) {
        tv_title.setText(resId)
    }

}