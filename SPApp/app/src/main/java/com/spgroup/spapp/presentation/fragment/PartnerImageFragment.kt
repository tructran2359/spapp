package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.loadImage
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.fragment_partner_image.*

class PartnerImageFragment: BaseFragment() {

    companion object {
        const val EXTRA_URL = "PartnerImageFragment.EXTRA_URL"
        const val PLACEHOLDER_PREFIX = "PLACEHOLDER_PREFIX"
        const val PLACEHOLDER_DIVIDER = "-"

        fun newInstance(url: String): PartnerImageFragment {
            val fragment = PartnerImageFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_URL, url)
            fragment.arguments = bundle
            return fragment
        }

        fun createPlaceholderUrl(cateId: String) = PLACEHOLDER_PREFIX + PLACEHOLDER_DIVIDER + cateId
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

        if (url.startsWith(PLACEHOLDER_PREFIX)) {
            val list = url.split(PLACEHOLDER_DIVIDER)
            if (list.size == 2) {
                val cateId = list[1]
                val placeholderResId = getPlaceholderResId(cateId)
                if (placeholderResId != -1) {
                    iv_content.setImageResource(placeholderResId)
                }
            }
        } else {
            iv_content.loadImage(url.toFullUrl())
        }
    }

    private fun getPlaceholderResId(cateId: String): Int {
        return when (cateId) {
            "food" -> R.drawable.placeholder_food
            "housekeeping" -> R.drawable.placeholder_housekeeping
            "groceries" -> R.drawable.placeholder_grocery
            "education" -> R.drawable.placeholder_education
            "laundry" -> R.drawable.placeholder_laundry
            "aircon" -> R.drawable.placeholder_aircon
            else -> -1
        }
    }
}