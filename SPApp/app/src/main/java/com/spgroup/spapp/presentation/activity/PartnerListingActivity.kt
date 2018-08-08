package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.presentation.adapter.PartnerAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerListingViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import kotlinx.android.synthetic.main.activity_partner_listing.*
import org.jetbrains.anko.longToast

class PartnerListingActivity: BaseActivity(), PartnerAdapter.OnItemClickListener {

    companion object {

        fun getLaunchIntent(context: Context, topLevelCategory: TopLevelCategory) : Intent {
            val intent = Intent(context, PartnerListingActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_TOP_LEVEL_CATE_NAME, topLevelCategory.name)
            intent.putExtra(ConstUtils.EXTRA_TOP_LEVEL_CATE_ID, topLevelCategory.id)
            return intent
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    val mAdapter = PartnerAdapter(this)
    var mCateName = ""
    var mCateId = -1
    lateinit var mViewModel: PartnerListingViewModel

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_listing)

        mCateId = intent.getIntExtra(ConstUtils.EXTRA_TOP_LEVEL_CATE_ID, -1)
        mCateName = intent.getStringExtra(ConstUtils.EXTRA_TOP_LEVEL_CATE_NAME)

        initViews()

        val factory = ViewModelFactory.getInstance()
        mViewModel = ViewModelProviders
                .of(this, factory)
                .get(PartnerListingViewModel::class.java)

        with(mViewModel) {
            partnerListing.observe(this@PartnerListingActivity, Observer {
                doLogD("PListing", "List: ${it?.size}")
                mAdapter.setData(it)
            })

            loadPartnerListing(-1)
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // PartnerAdapter.OnItemClickListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onItemClick(position: Int) {
        val partner = mViewModel.getPartner(position)
        partner?.let {
            if (it.isPromotion) {
                longToast("Promotion 's coming soon")
            } else {
                val intent = PartnerDetailsActivity.getLaunchIntent(this, it)
                startActivity(intent)
            }
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun initViews() {
        tv_title.setText(mCateName)
        iv_back.setOnClickListener {
            onBackPressed()
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
    }
}