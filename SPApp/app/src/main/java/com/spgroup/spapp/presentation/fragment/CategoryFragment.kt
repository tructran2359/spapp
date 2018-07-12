package com.spgroup.spapp.presentation.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.presentation.adapter.ServiceItemAdapter
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
        val fakeData = mutableListOf<ServiceItem>()
        for (i in 0 until 10) {
            val item = ServiceItem("Item $i", i, " / item", i, i, i)
            fakeData.add(item)
        }
        val fakeAdapter = ServiceItemAdapter()
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = fakeAdapter
        fakeAdapter.submitData(fakeData)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////
}