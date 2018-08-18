package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.presentation.fragment.PartnerImageFragment

class PartnerImagesAdapter(
        fragmentManager: FragmentManager,
        val urls: List<String>
): FragmentStatePagerAdapter(fragmentManager) {

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun getItem(position: Int): Fragment {
        return PartnerImageFragment.newInstance(urls[position])
    }

    override fun getCount() = urls.size


}