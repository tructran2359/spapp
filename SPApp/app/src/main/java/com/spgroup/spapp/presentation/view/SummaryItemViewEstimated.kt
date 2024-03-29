package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isGone
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_summary_item_estimated.view.*

class SummaryItemViewEstimated: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mDeleteListener: (() -> Unit)? = null

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context): super(context){
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle) {
        initViews(context)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_summary_item_estimated, this, true)

        iv_delete.setOnClickListener {
            mDeleteListener?.let {
                it()
            }
        }
    }

    fun setDescription(text: String?) {
        tv_text.setText(text)
        tv_text.isGone = text == null || text.isEmpty()
    }

    fun setName(name: String) {
        tv_name.setText(name)
    }

    fun setOnDeleteListener(action: (() -> Unit)?) {
        mDeleteListener = action
    }

}