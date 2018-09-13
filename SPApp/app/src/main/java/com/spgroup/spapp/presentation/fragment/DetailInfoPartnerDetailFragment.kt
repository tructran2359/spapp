package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.util.extension.addIndicatorText
import com.spgroup.spapp.util.extension.appInstance
import com.spgroup.spapp.util.extension.inflate
import com.spgroup.spapp.util.extension.obtainViewModelOfActivity
import kotlinx.android.synthetic.main.fragment_detail_info_partner_detail.*
import javax.inject.Inject

class DetailInfoPartnerDetailFragment: BaseFragment() {

    private lateinit var mViewModel: PartnerDetailsViewModel
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_detail_info_partner_detail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appInstance.appComponent.inject(this)

        mViewModel = obtainViewModelOfActivity(PartnerDetailsViewModel::class.java, vmFactory)
        subscribeUI()
    }

    private fun subscribeUI() {
        mViewModel.run {
            partnerDetails.observe(this@DetailInfoPartnerDetailFragment, Observer {
                it?.let { partnerDetails ->
                    val title = partnerDetails.serviceInfo?.title ?: ""
                    tv_title.text = title
                    tv_title.isGone = title.isEmpty()

                    val description = partnerDetails.serviceInfo?.description ?: ""
                    tv_description.text = description
                    tv_description.isGone = description.isEmpty()

                    activity?.let {
                        ll_list.addIndicatorText(partnerDetails.serviceInfo?.list)
                    }

                }
            })
        }
    }


}