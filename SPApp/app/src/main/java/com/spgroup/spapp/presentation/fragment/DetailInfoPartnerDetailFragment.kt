package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.addIndicatorText
import com.spgroup.spapp.util.extension.inflate
import com.spgroup.spapp.util.extension.obtainViewModelOfActivity
import kotlinx.android.synthetic.main.fragment_detail_info_partner_detail.*

class DetailInfoPartnerDetailFragment: BaseFragment() {

    private lateinit var mViewModel: PartnerDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_detail_info_partner_detail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = obtainViewModelOfActivity(PartnerDetailsViewModel::class.java, ViewModelFactory.getInstance())
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