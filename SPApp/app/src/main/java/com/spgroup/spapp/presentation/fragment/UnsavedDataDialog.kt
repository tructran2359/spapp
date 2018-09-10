package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.View
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.dialog_unsaved_data.*

class UnsavedDataDialog: BaseDialog() {

    var actionYes: (() -> Unit)? = null
    var actionNo: (() -> Unit)? = null

    override fun getLayoutResId() = R.layout.dialog_unsaved_data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_yes.setOnClickListener {
            dismiss()
            actionYes?.let {
                it()
            }
        }

        tv_no.setOnClickListener {
            dismiss()
            actionNo?.let {
                it()
            }
        }
    }

    fun setActions(actionYes: (() -> Unit)?, actionNo: (() -> Unit)?) {
        this.actionYes = actionYes
        this.actionNo = actionNo
    }

}