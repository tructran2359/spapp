package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.fragment_weekly_menu.*

class WeeklyMenuFragment: BaseFragment() {

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_weekly_menu, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menu_view_1.addMenu("3 Dishes Plus 1 Soup Meal Set")
        menu_view_1.addMenu("4 Dishes")

        menu_view_2.addMenu("3 Dishes Plus 1 Soup Meal Set")
        menu_view_2.addMenu("4 Dishes")
    }
}