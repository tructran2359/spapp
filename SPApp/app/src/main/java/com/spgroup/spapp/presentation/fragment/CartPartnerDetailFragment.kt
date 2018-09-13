package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.CategoryPagerAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.fragment_cart_partner_detail.*
import javax.inject.Inject

class CartPartnerDetailFragment: BaseFragment() {

    private lateinit var mViewModel: PartnerDetailsViewModel
    private lateinit var mAdapter: CategoryPagerAdapter

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_cart_partner_detail, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appInstance.appComponent.inject(this)

        setUpPages()

        mViewModel = obtainViewModelOfActivity(PartnerDetailsViewModel::class.java, vmFactory)
        subcribeUI()
    }

    private fun subcribeUI() {
        mViewModel.run {
            partnerDetails.observe(this@CartPartnerDetailFragment, Observer {
                it?.let {
                    doLogD("EmptyResques", "partner detail update UI")
                    mAdapter.setData(it.categories, it.menu)
                    setUpTabLayout()
                }
            })

            refreshData.observe(this@CartPartnerDetailFragment, Observer {
                it?.let { refresh ->
                    if (refresh) {
                        pager_forms.setCurrentItem(0, false)
                    }
                }
            })
        }
    }

    private fun setUpPages() {
        mAdapter = CategoryPagerAdapter(childFragmentManager)

        pager_forms.offscreenPageLimit = 3
        pager_forms.adapter = mAdapter

        tab_layout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //Do nothing
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val textView = customView?.findViewById<TextView>(R.id.tv_content)
                textView?.setUpMenuInactive()
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val textView = customView?.findViewById<TextView>(R.id.tv_content)
                textView?.setUpMenuActive()
            }

        })
    }

    private fun setUpTabLayout() {
        tab_layout.setupWithViewPager(pager_forms)
        for (i in 0 until tab_layout.tabCount) {
            val customView = LayoutInflater.from(activity).inflate(R.layout.view_custom_service_cate_tab, null, false)
            val tvContent = customView.findViewById<TextView>(R.id.tv_content)
            tvContent.text = mAdapter.getPageTitle(i)
            if (i == 0) {
                tvContent.setUpMenuActive()
            } else {
                tvContent.setUpMenuInactive()
            }
            val tab = tab_layout.getTabAt(i)
            tab?.customView = customView
        }
    }
}