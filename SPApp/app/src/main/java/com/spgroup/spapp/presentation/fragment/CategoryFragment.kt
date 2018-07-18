package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceGroup
import com.spgroup.spapp.domain.model.ServiceItemCheckBox
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.domain.model.ServiceItemCounter
import com.spgroup.spapp.presentation.adapter.CategoryServiceAdapter
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment: BaseFragment() {

    companion object {
        @JvmField val KEY_CATEGORY = "CategoryFragment.KEY_CATEGORY"

        fun newInstance(category: String): CategoryFragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            bundle.putString(KEY_CATEGORY, category)
            fragment.arguments = bundle
            return fragment
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mCategoryName: String
    lateinit var mServiceAdapter: CategoryServiceAdapter

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCategoryName = arguments?.getString(KEY_CATEGORY) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item1 = ServiceItemCounter(
                "Paint",
                10f,
                " / item")

        val item2 = ServiceItemCheckBox(
                "Day Curtains",
                10f,
                " / piece",
                "Also known as sheers, made of light coloured materials to allow light in from outside.",
                false)

        val item3 = ServiceItemCheckBox(
                "Night Curtains",
                9f,
                " / kg",
                "Heavier in weight and are of dark coloured materials. These are drawn at night for privacy.",
                false
        )

        val item4 = ServiceItemCombo("3 Dishes Plus 1 Soup Meal Set",
                165f,
                " per month",
                "Weekdays only. Island-wide delivery. Packed in microwavable containers only.",
                false)

        val data1 = ServiceGroup("GARMENTS",
                "Includes free dismantling & installation. Measurement & evaluation will be done on-site, price estimation will not be included in this request.",
                mutableListOf(item1, item2, item3, item4),
                false)
        val data2 = data1.copy(name = "GARMENTS 2")
        val fakeData = mutableListOf(data1, data2)

        activity?.let {
            mServiceAdapter = CategoryServiceAdapter(it)
            mServiceAdapter.submitData(fakeData)
            recycler_view.layoutManager = LinearLayoutManager(it)
            recycler_view.adapter = mServiceAdapter
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////
}