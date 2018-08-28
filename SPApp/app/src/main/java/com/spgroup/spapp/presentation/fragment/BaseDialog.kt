package com.spgroup.spapp.presentation.fragment

import android.content.DialogInterface
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager

open class BaseDialog: DialogFragment() {

    private var showing = false

    override fun show(manager: FragmentManager?, tag: String?) {
        if (showing) return

        super.show(manager, tag)
        showing = true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        showing = false
    }

    fun isShowing() = showing
}