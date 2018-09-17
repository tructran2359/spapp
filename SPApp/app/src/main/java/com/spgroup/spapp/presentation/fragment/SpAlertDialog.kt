package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.View
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.dialog_alert.*

class SpAlertDialog: BaseDialog() {

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESC = "EXTRA_DESC"
        const val EXTRA_POSITIVE = "EXTRA_POSITIVE"
        const val EXTRA_NEGATIVE = "EXTRA_NEGATIVE"

        fun getInstance(title: String, description: String, positiveText: String, negativeText: String): SpAlertDialog {
            val dialog = SpAlertDialog()
            val bundle = Bundle()
            bundle.putString(EXTRA_TITLE, title)
            bundle.putString(EXTRA_DESC, description)
            bundle.putString(EXTRA_POSITIVE, positiveText)
            bundle.putString(EXTRA_NEGATIVE, negativeText)
            dialog.arguments = bundle
            return dialog
        }
    }

    private var actionPositive: (() -> Unit)? = null
    private var actionNegative: (() -> Unit)? = null

    override fun getLayoutResId() = R.layout.dialog_alert

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            tv_title.text = getString(EXTRA_TITLE)
            tv_description.text = getString(EXTRA_DESC)
            tv_positive.text = getString(EXTRA_POSITIVE)
            tv_negative.text = getString(EXTRA_NEGATIVE)

            tv_positive.setOnClickListener {
                actionPositive?.invoke()
            }

            tv_negative.setOnClickListener {
                actionNegative?.invoke()
            }
        }


    }

    fun setPositiveAction(action: () -> Unit) {
        actionPositive = action
    }

    fun setNegativeAction(action: () -> Unit) {
        actionNegative = action
    }

}