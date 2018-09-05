package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.isGoneWithText
import kotlinx.android.synthetic.main.layout_summary_view_item_combo.view.*

class SummaryItemViewCombo: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    private var mDeleteListener: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle) {
        initViews(context)
    }

    private fun initViews(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_summary_view_item_combo, this, true)

        iv_delete.setOnClickListener {
            mDeleteListener?.let {
                it()
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun addOption(optionName: String, optionPrice: Float) {
        val optionView = PriceTextView(context)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = context.getDimensionPixelSize(R.dimen.common_vert_medium_sub)
        optionView.layoutParams = layoutParams
        optionView.setName(optionName)
        optionView.setPrice(optionPrice)
        ll_option_container.addView(optionView)
    }

    fun clearOption() {
        ll_option_container.removeAllViews()
    }

    fun setInstruction(instruction: String) {
        tv_instruction.text = instruction
        tv_instruction.isGoneWithText(instruction)
        tv_instruction_label.isGoneWithText(instruction)
    }

    fun setServiceName(name: String) {
        tv_service_name.text = name
    }

    fun setServiceDescription(description: String?) {
        tv_service_description.text = description
        tv_service_description.isGone = description == null || description.isEmpty()
    }

    fun setOnEditClickListener(action: () -> Unit) {
        iv_edit.setOnClickListener {
            action()
        }
    }

    fun setOnDeleteListener(action: (() -> Unit)?) {
        mDeleteListener = action
    }
}