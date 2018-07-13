package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.CategoryService
import com.spgroup.spapp.domain.model.CheckBoxServiceItem
import com.spgroup.spapp.domain.model.CounterServiceItem
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

        val item1 = CounterServiceItem(
                "Paint",
                10,
                " / item")

        val item2 = CheckBoxServiceItem(
                "Day Curtains",
                10,
                " / piece",
                "Also known as sheers, made of light coloured materials to allow light in from outside.",
                false)

        val item3 = CheckBoxServiceItem(
                "Night Curtains",
                9,
                " / kg",
                "Heavier in weight and are of dark coloured materials. These are drawn at night for privacy.",
                false
        )

        val data1 = CategoryService("GARMENTS", mutableListOf(item1, item2, item3), false)
        val data2 = CategoryService("GARMENTS 2", mutableListOf(item1, item2, item3), false)
        val fakeData = mutableListOf(data1, data2)

        mServiceAdapter = CategoryServiceAdapter()
        mServiceAdapter.submitData(fakeData)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = mServiceAdapter
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////
}