package com.spgroup.spapp.presentation.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.spgroup.spapp.presentation.fragment.OnBoardingFragment

class OnBoardingAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int) = OnBoardingFragment.newInstance(position)

    override fun getCount() = 4
}