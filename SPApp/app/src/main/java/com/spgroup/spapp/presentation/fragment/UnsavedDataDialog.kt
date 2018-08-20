package com.spgroup.spapp.presentation.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.dialog_unsaved_data.*

class UnsavedDataDialog: DialogFragment() {

    var actionYes: (() -> Unit)? = null
    var actionNo: (() -> Unit)? = null
    var showing = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_unsaved_data, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)

        return view
    }

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

    override fun show(manager: FragmentManager?, tag: String?) {
        if (showing) return

        super.show(manager, tag)
        showing = true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        showing = false
    }

    fun setActions(actionYes: (() -> Unit)?, actionNo: (() -> Unit)?) {
        this.actionYes = actionYes
        this.actionNo = actionNo
    }

    fun isShowing() = showing
}