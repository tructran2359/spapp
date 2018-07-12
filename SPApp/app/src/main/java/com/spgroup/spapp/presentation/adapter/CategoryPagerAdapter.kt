package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.presentation.fragment.CategoryFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mData = mutableListOf<String>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun getItem(position: Int): Fragment {
        return CategoryFragment.newInstance(mData[position])
    }

    override fun getCount() = mData.size

    override fun getPageTitle(position: Int) = mData[position]

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setData(data: List<String>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }
}