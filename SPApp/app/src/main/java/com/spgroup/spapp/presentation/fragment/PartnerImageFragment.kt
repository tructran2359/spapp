package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.loadImageWithPlaceholder
import com.spgroup.spapp.util.extension.toFullUrl
import kotlinx.android.synthetic.main.fragment_partner_image.*

class PartnerImageFragment: BaseFragment() {

    companion object {
        const val EXTRA_URL = "PartnerImageFragment.EXTRA_URL"
        const val EXTRA_CATE_ID = "PartnerImageFragment.EXTRA_CATE_ID"
        const val PLACEHOLDER = "PartnerImageFragment.PLACEHOLDER"

        fun newInstance(url: String, cateId: String): PartnerImageFragment {
            val fragment = PartnerImageFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_URL, url)
            bundle.putString(EXTRA_CATE_ID, cateId)
            fragment.arguments = bundle
            return fragment
        }

    }

    private lateinit var url: String
    private lateinit var cateId: String

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(EXTRA_URL, "") ?: ""
        cateId = arguments?.getString(EXTRA_CATE_ID, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_partner_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeHolderResId = getPlaceholderResId()

        if (url.startsWith(PLACEHOLDER)) {
            val placeholderResId = placeHolderResId
            if (placeholderResId != -1) {
                iv_content.setImageResource(placeholderResId)
            }
        } else {
            iv_content.loadImageWithPlaceholder(url.toFullUrl(), 0, placeHolderResId)
        }
    }

    private fun getPlaceholderResId(): Int {
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