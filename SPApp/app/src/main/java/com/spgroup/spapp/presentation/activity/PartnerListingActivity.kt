package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelCategory
import com.spgroup.spapp.presentation.adapter.PartnerAdapter
import com.spgroup.spapp.presentation.viewmodel.PartnerListingViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.extension.obtainViewModel
import com.spgroup.spapp.util.extension.toFullImgUrl
import kotlinx.android.synthetic.main.activity_partner_listing.*
import org.jetbrains.anko.longToast

class PartnerListingActivity : BaseActivity(), PartnerAdapter.OnItemClickListener {

    companion object {

        fun getLaunchIntent(context: Context, topLevelCategory: TopLevelCategory) =
                Intent(context, PartnerListingActivity::class.java).apply {
                    putExtra(ConstUtils.EXTRA_TOP_LEVEL_CATEGORY, topLevelCategory)
                }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mViewModel: PartnerListingViewModel
    private val mPartnerListAdapter = PartnerAdapter(this)

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_listing)
        initViews()

        val cat = intent.getSerializableExtra(ConstUtils.EXTRA_TOP_LEVEL_CATEGORY) as TopLevelCategory
        mViewModel = obtainViewModel(PartnerListingViewModel::class.java, ViewModelFactory.getInstance())
        mViewModel.setInitialData(cat)

        with(mViewModel) {
            partnerListing.observe(this@PartnerListingActivity, Observer {
                mPartnerListAdapter.setData(it)
            })
            topLevelCategory.observe(this@PartnerListingActivity, Observer {
                it?.let { updateBanner(it) }
            })
            loadPartnerListing(-1)
        }
    }

    private fun updateBanner(topLevelCategory: TopLevelCategory) {
        tv_title.text = topLevelCategory.name
        Glide.with(this@PartnerListingActivity)
                .load(topLevelCategory.banner.toFullImgUrl())
                .into(iv_banner)
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
        iv_back.setOnClickListener {
            onBackPressed()
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mPartnerListAdapter
    }
}