package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.domain.model.FoodMenu
import com.spgroup.spapp.presentation.fragment.CategoryFragment
import com.spgroup.spapp.presentation.fragment.WeeklyMenuFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private val mData = mutableListOf<Category>()
    private var mFoodMenu: FoodMenu? = null

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun getItem(position: Int): Fragment {
        if (hasMenu()) {
            if (position < mData.size) {
                return CategoryFragment.newInstance(mData[position].id)
            } else {
                return WeeklyMenuFragment()
            }
        } else {
            return CategoryFragment.newInstance(mData[position].id)
        }
    }

    override fun getCount() = mData.size + if (hasMenu()) 1 else 0

    override fun getPageTitle(position: Int) =
            if (position < mData.size) {
                mData[position].label
            } else {
                mFoodMenu?.tabTitle
            }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setData(data: List<Category>?, foodMenu: FoodMenu?) {
        mData.clear()
        data?.let {
            mData.addAll(it)
        }
        mFoodMenu = foodMenu
        notifyDataSetChanged()
    }

    private fun hasMenu() = mFoodMenu != null
}