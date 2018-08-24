package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.view.IndicatorTextView
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.getDimensionPixelSize
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
                it?.let {
                    tv_title.text = it.serviceInfo?.title ?: ""
                    tv_description.text = it.serviceInfo?.description ?: ""

                    activity?.let { activity ->
                        it.serviceInfo?.list?.forEach { offer ->
                            if (!offer.isEmpty()) {
                                val indicatorTextView = IndicatorTextView(activity, offer)
                                val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                                layoutParams.topMargin = activity.getDimensionPixelSize(R.dimen.common_vert_medium_sub)
                                indicatorTextView.layoutParams = layoutParams
                                ll_list.addView(indicatorTextView)
                            }
                        }
                    }

                }
            })
        }
    }


}