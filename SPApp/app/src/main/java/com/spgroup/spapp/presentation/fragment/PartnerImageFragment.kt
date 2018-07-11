package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.fragment_partner_image.*

class PartnerImageFragment: BaseFragment() {

    companion object {
        @JvmField val EXTRA_POSITION = "PartnerImageFragment.EXTRA_POSITION"

        fun newInstance(position: Int): PartnerImageFragment {
            val fragment = PartnerImageFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    var mPosition = 0

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPosition = arguments?.getInt(EXTRA_POSITION, 0) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_partner_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(mPosition) {
            0 -> iv_content.setImageResource(R.drawable.laundry01)
            1 -> iv_content.setImageResource(R.drawable.laundry02)
            2 -> iv_content.setImageResource(R.drawable.laundry03)
            3 -> iv_content.setImageResource(R.drawable.laundry04)
        }
    }
}