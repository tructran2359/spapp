package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.formatPrice
import kotlinx.android.synthetic.main.dialog_minimum_order.*

class MinOrderDialog: BaseDialog() {

    companion object {
        const val EXTRA_MIN_PRICE = "EXTRA_MIN_PRICE"

        fun getInstance(minPrice: Float): MinOrderDialog {
            val dialog = MinOrderDialog()
            val bundle = Bundle()
            bundle.putFloat(EXTRA_MIN_PRICE, minPrice)
            dialog.arguments = bundle
            return dialog
        }
    }

    private var actionContinue: (() -> Unit)? = null
    private var actionAddMore: (() -> Unit)? = null

    override fun getLayoutResId() = R.layout.dialog_minimum_order

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_continue.setOnClickListener {
            actionContinue?.invoke()
        }

        tv_add_more.setOnClickListener {
            actionAddMore?.invoke()
        }

        val minPrice = arguments?.getFloat(EXTRA_MIN_PRICE, 0f) ?: 0f
        tv_title.text = getString(R.string.dialog_min_order_title, minPrice.formatPrice())

    }

    fun setOnContinueListener(action: () -> Unit) {
        actionContinue = action
    }

    fun setOnAddMoreListener(action: () -> Unit) {
        actionAddMore = action
    }

}