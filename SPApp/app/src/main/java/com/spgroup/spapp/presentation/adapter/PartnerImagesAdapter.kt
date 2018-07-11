package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.presentation.fragment.PartnerImageFragment
import com.spgroup.spapp.util.ConstUtils

class PartnerImagesAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun getItem(position: Int): Fragment {
        return PartnerImageFragment.newInstance(position)
    }

    override fun getCount() = ConstUtils.PARTNERS_IMAGE_COUNT


}