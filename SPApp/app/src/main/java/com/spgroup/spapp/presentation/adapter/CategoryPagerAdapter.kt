package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.presentation.fragment.CategoryFragment
import com.spgroup.spapp.presentation.fragment.WeeklyMenuFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mData = mutableListOf<ServiceCategory>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun getItem(position: Int): Fragment {
        val category = mData[position]

        return when(category.type) {
            ServiceCategory.TYPE_WEEKLY_MENU -> WeeklyMenuFragment()

            ServiceCategory.TYPE_LAUNDRY -> CategoryFragment.newInstance(category)

            else -> Fragment()
        }
    }

    override fun getCount() = mData.size

    override fun getPageTitle(position: Int) = mData[position].title

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setData(data: List<ServiceCategory>?) {
        mData.clear()
        data?.let {
            mData.addAll(it)
        }
        notifyDataSetChanged()
    }
}