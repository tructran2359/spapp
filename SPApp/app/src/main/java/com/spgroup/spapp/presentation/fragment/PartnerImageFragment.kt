package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.loadImage
import com.spgroup.spapp.util.extension.toFullImgUrl
import kotlinx.android.synthetic.main.fragment_partner_image.*

class PartnerImageFragment: BaseFragment() {

    companion object {
        const val EXTRA_URL = "PartnerImageFragment.EXTRA_URL"

        fun newInstance(url: String): PartnerImageFragment {
            val fragment = PartnerImageFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var url: String

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(EXTRA_URL, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_partner_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_content.loadImage(url.toFullImgUrl())
    }
}