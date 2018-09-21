package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.FoodMenuItem
import com.spgroup.spapp.presentation.activity.BaseActivity
import com.spgroup.spapp.presentation.activity.PdfActivity
import com.spgroup.spapp.presentation.view.MenuView
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.appInstance
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.obtainViewModelOfActivity
import kotlinx.android.synthetic.main.fragment_weekly_menu.*
import javax.inject.Inject

class WeeklyMenuFragment: BaseFragment(), MenuView.OnMenuItemClickListener {

    private lateinit var mViewModel: PartnerDetailsViewModel
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_weekly_menu, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = obtainViewModelOfActivity(PartnerDetailsViewModel::class.java, vmFactory)
        subscribeUI()
    }

    private fun subscribeUI() {
        mViewModel.run {
            partnerDetails.observe(this@WeeklyMenuFragment, Observer {
                it?.let { partnerDetails ->
                    partnerDetails.menu?.let { menu ->
                        tv_name.text = menu.title
                        tv_description.text = menu.summary
                        var isFirstItem = true
                        menu.items?.forEach { foodMenuItem ->
                            if (foodMenuItem.isValid()) {
                                if (isFirstItem) {
                                    isFirstItem = false
                                    addMenuItem(foodMenuItem)
                                } else {
                                    addDivider()
                                    addMenuItem(foodMenuItem)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun addMenuItem(foodMenuItem: FoodMenuItem) {
        if (foodMenuItem.label == null) return
        activity?.run {
            val menuView = MenuView(this)
            menuView.run {
                setMenuName(foodMenuItem.label)
                foodMenuItem.pdfs?.forEach { pdf ->
                    this.addPdf(pdf)
                }
                setOnMenuItemClickListener(this@WeeklyMenuFragment)
            }
            ll_menu_container.addView(menuView)
        }

    }

    private fun addDivider() {
        val view = View(activity)
        val height = activity?.getDimensionPixelSize(R.dimen.line_width) ?: 1
        val marginHorz = activity?.getDimensionPixelSize(R.dimen.common_horz_medium) ?: 0
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, height)
        layoutParams.run {
            leftMargin = marginHorz
            rightMargin = marginHorz
        }
        view.layoutParams = layoutParams
        view.setBackgroundResource(R.color.color_ui04)
        ll_menu_container.addView(view)
    }

    ///////////////////////////////////////////////////////////////////////////
    // MenuView.OnMenuItemClickListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onMenuItemClick(title: String, uri: String) {
        doLogD("PDF", "Title: $title | Uri: $uri")
        activity?.run {
            val intent = PdfActivity.getLaunchIntent(this, title, uri)
            (this as BaseActivity).startActivityWithCheckingInternet(intent)
        }
    }
}