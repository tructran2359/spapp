package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.layout_validation_input_view.view.*

class ValidationInputView: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private var mLabel = ""
    private var mValidate: ((text: String) -> Boolean)? = null

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initViews(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle) {
        initViews(context, attributeSet)
    }

    private fun initViews(context: Context, attributeSet: AttributeSet) {

        initProperties(context, attributeSet)

        LayoutInflater.from(context).inflate(R.layout.layout_validation_input_view, this, true)
        tv_label.setText(mLabel)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initProperties(context: Context, attributeSet: AttributeSet) {
        var typedArray = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.ValidationInputView,
                0,
                0)

        try {
            mLabel = typedArray.getString(R.styleable.ValidationInputView_label)
        } finally {
            typedArray.recycle()
        }
    }

    fun setValidation(action: (text: String) -> Boolean) {
        mValidate = action
    }

    fun setInputType(inputType: Int) {
        et_input.inputType = inputType
    }

    fun validate() : Boolean {
        val text = et_input.text.toString()
        var valid = true
        if (text.isEmpty()) {
            valid = false
            tv_alert.setText(R.string.required)
        } else {
            mValidate?.let {
                valid = it(text)
            }

            if (!valid) {
                tv_alert.setText(R.string.invalid)
            }
        }

        et_input.isSelected = !valid
        tv_alert.visibility = if (valid) View.GONE else View.VISIBLE

        return valid
    }
}