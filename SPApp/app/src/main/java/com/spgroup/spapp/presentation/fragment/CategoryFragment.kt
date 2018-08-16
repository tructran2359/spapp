package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.domain.model.ServiceItemCounter
import com.spgroup.spapp.domain.model.ServiceCategory
import com.spgroup.spapp.presentation.activity.CustomiseActivity
import com.spgroup.spapp.presentation.adapter.ServiceGroupAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment: BaseFragment(), ServiceGroupAdapter.OnItemInteractedListener {

    companion object {
        @JvmField val KEY_CATEGORY = "CategoryFragment.KEY_CATEGORY"

        fun newInstance(category: ServiceCategory): CategoryFragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CATEGORY, category)
            fragment.arguments = bundle
            return fragment
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mServiceAdapter: ServiceGroupAdapter
    lateinit var mViewModel: PartnerDetailsViewModel
    lateinit var mServiceCategory: ServiceCategory

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mServiceCategory = it.getSerializable(KEY_CATEGORY) as ServiceCategory
        }

        mServiceAdapter = ServiceGroupAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance()
        mViewModel = ViewModelProviders.of(activity!!, factory).get(PartnerDetailsViewModel::class.java)

        activity?.let {

            mServiceAdapter = ServiceGroupAdapter(this@CategoryFragment)
            mServiceAdapter.submitData(mServiceCategory.services)
            recycler_view.layoutManager = LinearLayoutManager(it)
            recycler_view.adapter = mServiceAdapter
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // OnItemInteractedListener
    ///////////////////////////////////////////////////////////////////////////
    override fun onCollapseClick(position: Int) {
        mServiceAdapter.collapseItem(position)
    }

    override fun onServiceItemClick(servicePos: Int, itemPos: Int) {
        val serviceItem = mServiceAdapter.getItem(servicePos, itemPos)
            activity?.let {
                if (serviceItem is ServiceItemCombo) {
                    val intent = CustomiseActivity.getLaunchIntent(it, serviceItem)
                    it.startActivity(intent)
                }
            }

    }

    override fun onCountChanged(count: Int, servicePos: Int, itemPos: Int) {
        val serviceItem = (mServiceCategory.getServiceItem(servicePos, itemPos) as ServiceItemCounter)
        serviceItem.count = count
        mViewModel.updateSelectedServiceCategories(serviceItem.count, mServiceCategory.id, servicePos, itemPos)
    }

    override fun onCheckChanged(checked: Boolean, servicePos: Int, itemPos: Int) {
        val serviceItem = (mServiceCategory.getServiceItem(servicePos, itemPos) as ServiceItemCheckBox)
        serviceItem.selected = checked
        mViewModel.updateSelectedServiceCategories(serviceItem.getItemCount(), mServiceCategory.id, servicePos, itemPos)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

}