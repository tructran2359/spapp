package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.presentation.fragment.CategoryFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private val mData = mutableListOf<Category>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun getItem(position: Int): Fragment {
        return CategoryFragment.newInstance(mData[position].id)
    }

    override fun getCount() = mData.size

    override fun getPageTitle(position: Int) = mData[position].label

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setData(data: List<Category>?) {
        mData.clear()
        data?.let {
            mData.addAll(it)
        }
        notifyDataSetChanged()
    }
}