package com.spgroup.spapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.CustomiseViewModel
import kotlinx.android.synthetic.main.layout_summary_view_item_combo.view.*

class SummaryItemViewCombo: RelativeLayout {

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mViewPax: PriceTextView
    private lateinit var mViewRice: PriceTextView
    private var content = CustomiseViewModel.Content()

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
        mViewPax = PriceTextView(context)
        mViewRice = PriceTextView(context)

        updateUI()

        ll_option_container.addView(mViewPax)
        ll_option_container.addView(mViewRice)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setDummyData(content: CustomiseViewModel.Content) {
        this.content = content
        updateUI()
    }

    fun getDummyData() = content

    private fun updateUI() {
        mViewRice.run {
            setName("${content.riceCount} Plain rice")
            setPrice(content.ricePrice * content.riceCount)
        }

        mViewPax.run {
            setName("${content.paxCount} Pax")
            setPrice(content.paxPrice * content.paxCount)
        }

        tv_instruction.setText(content.instruction)
    }

    fun addOption(optionName: String, optionPrice: Float) {
        val optionView = PriceTextView(context)
        optionView.setName(optionName)
        optionView.setPrice(optionPrice)
        ll_option_container.addView(optionView)
    }

    fun clearOption() {
        ll_option_container.removeAllViews()
    }

    fun setOnEditClickListener(action: () -> Unit) {
        iv_edit.setOnClickListener {
            action()
        }
    }
}