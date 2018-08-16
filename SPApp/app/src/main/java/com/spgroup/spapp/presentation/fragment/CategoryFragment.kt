package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.presentation.adapter.ServiceListingAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseFragment(), ServiceListingAdapter.OnItemInteractedListener {

    companion object {
        const val KEY_CATEGORY_ID = "CategoryFragment.KEY_CATEGORY_ID"

        fun newInstance(categoryId: String): CategoryFragment {
            return CategoryFragment().apply {
                val bundle = Bundle()
                bundle.putString(KEY_CATEGORY_ID, categoryId)
                arguments = bundle
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mServiceListingAdapter: ServiceListingAdapter
    lateinit var mViewModel: PartnerDetailsViewModel
    private var mCategory: Category? = null

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProviders
                .of(activity!!, ViewModelFactory.getInstance())
                .get(PartnerDetailsViewModel::class.java)

        arguments?.let {
            if (it.getString(KEY_CATEGORY_ID) == null) {
                throw IllegalArgumentException("Category must be provided")
            } else {
                val categoryId = it.getString(KEY_CATEGORY_ID)
                mCategory = mViewModel.getCategory(categoryId)
            }
        } ?: throw IllegalArgumentException("Category must be provided")

        activity?.let {
            mServiceListingAdapter = ServiceListingAdapter(this@CategoryFragment)
            mServiceListingAdapter.submitData(mCategory?.subCategories)
            recycler_view.layoutManager = LinearLayoutManager(it)
            recycler_view.adapter = mServiceListingAdapter
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // OnItemInteractedListener
    ///////////////////////////////////////////////////////////////////////////
    override fun onCollapseClick(position: Int) {
        mServiceListingAdapter.collapseItem(position)
    }

    override fun onServiceItemClick(servicePos: Int, itemPos: Int) {
//        val serviceItem = mServiceListingAdapter.getItem(servicePos, itemPos)
//        activity?.let {
//            if (serviceItem is ServiceItemCombo) {
//                val intent = CustomiseActivity.getLaunchIntent(it, serviceItem)
//                it.startActivity(intent)
//            }
//        }

    }

    override fun onCountChanged(count: Int, servicePos: Int, itemPos: Int) {
//        val serviceItem = (mCategoryId.getServiceItem(servicePos, itemPos) as ServiceItemCounter)
//        serviceItem.count = count
//        mViewModel.updateSelectedServiceCategories(serviceItem.count, mCategoryId.id.toString(), "")
    }

    override fun onCheckChanged(checked: Boolean, servicePos: Int, itemPos: Int) {
//        val serviceItem = (mCategoryId.getServiceItem(servicePos, itemPos) as ServiceItemCheckBox)
//        serviceItem.selected = checked
//        mViewModel.updateSelectedServiceCategories(serviceItem.getItemCount(), mCategoryId.id.toString(), "")
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

}