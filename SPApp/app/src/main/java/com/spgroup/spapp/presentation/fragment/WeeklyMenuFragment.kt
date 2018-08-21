package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.view.MenuView
import kotlinx.android.synthetic.main.fragment_weekly_menu.*

class WeeklyMenuFragment: BaseFragment(), MenuView.OnMenuItemClickListener {

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_weekly_menu, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(menu_view_1) {
            setMenuName("Menu for 2 Jul - 6 Jul")
            setOnMenuItemClickListener(this@WeeklyMenuFragment)
            addMenuItem("3 Dishes Plus 1 Soup Meal Set\nSpecial Meal Set")
            addMenuItem("4 Dishes")
        }

        with(menu_view_2) {
            setMenuName("Menu for 9 Jul - 13 Jul")
            setOnMenuItemClickListener(this@WeeklyMenuFragment)
            addMenuItem("3 Dishes Plus 1 Soup Meal Set")
            addMenuItem("4 Dishes")
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // MenuView.OnMenuItemClickListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onMenuItemClick(menuItem: String) {
        //TODO: Implement later
//        activity?.let {
//            val intent = PdfActivity.getLaunchIntent(it)
//            it.startActivity(intent)
//        }
    }
}