package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.MultiplierService
import com.spgroup.spapp.presentation.activity.BaseActivity
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import com.spgroup.spapp.presentation.activity.CustomiseNewActivity
import com.spgroup.spapp.presentation.adapter.ServiceListingAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.toInt
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

        subcribeUI()

        arguments?.let {
            val categoryId = it.getString(KEY_CATEGORY_ID)
            if (categoryId == null) {
                throw IllegalArgumentException("Category must be provided")
            } else {
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

    private fun subcribeUI() {
        mViewModel.run {
            newSelectedComplexServiceWithCateId.observe(this@CategoryFragment, Observer { pair ->
                pair?.run {
                    if (mCategory!!.id == first) {
                        mServiceListingAdapter.addSelectedItem(second, 1)
                    }
                }
            })

            refreshData.observe(this@CategoryFragment, Observer {
                it?.let { clear ->
                    if (clear) {
                        mServiceListingAdapter.refreshDataState()
                    }
                }
            })
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // OnItemInteractedListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onComplexCustomisationItemClick(itemData: ComplexCustomisationService) {
        activity?.let {
            val cateId = mCategory!!.id
            val serviceId = itemData.getServiceId()
            val displayData = CustomiseDisplayData(
                    categoryId = mCategory!!.id,
                    serviceItem = itemData,
                    mapSelectedOption = mViewModel.getSelectedOptionMap(cateId, serviceId) ?: HashMap(),
                    specialInstruction = mViewModel.getSelectedInstruction(cateId, serviceId),
                    subCateName = mViewModel.getSubCateName(cateId, serviceId)
            )
            val intent = CustomiseNewActivity.getLaunchIntent(
                    context = it,
                    displayData = displayData,
                    isEdit = false)
            it.startActivityForResult(intent, BaseActivity.RC_CUSTOMISE)
        }
    }

    override fun onComplexCustomisationItemDelete(itemData: ComplexCustomisationService) {
        mViewModel.removeSelectedService(mCategory!!.id, itemData.id)
    }

    override fun onMultiplierItemChanged(itemData: MultiplierService, count: Int) {
        mViewModel.updateNormalSelectedServiceItem(
                absServiceItem = itemData,
                count = count,
                categoryId = mCategory!!.id
        )
    }

    override fun onCheckboxItemChanged(itemData: CheckboxService, checked: Boolean) {
        mViewModel.updateNormalSelectedServiceItem(
                absServiceItem = itemData,
                count = checked.toInt(),
                categoryId = mCategory!!.id
        )
    }

}