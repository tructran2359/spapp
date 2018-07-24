package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.PartnerAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerListingViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.doLogD
import kotlinx.android.synthetic.main.activity_partner_listing.*

class PartnerListingActivity: BaseActivity() {

    companion object {

        fun getLaunchIntent(context: Context) : Intent {
            val intent = Intent(context, PartnerListingActivity::class.java)
            return intent
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mAdapter = PartnerAdapter()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_listing)

        initViews()

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProviders
                .of(this, factory)
                .get(PartnerListingViewModel::class.java)

        with(viewModel) {
            partnerListing.observe(this@PartnerListingActivity, Observer {
                doLogD("PListing", "List: ${it?.size}")
                mAdapter.setData(it)
            })

            loadPartnerListing(-1)
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun initViews() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
    }
}