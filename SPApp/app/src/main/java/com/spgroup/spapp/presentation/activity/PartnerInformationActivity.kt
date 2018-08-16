package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.view.IndicatorTextView
import kotlinx.android.synthetic.main.activity_partner_information.*
import java.io.Serializable

class PartnerInformationActivity : BaseActivity() {

    companion object {

        const val EXTRA_PARTNER_INFO = "EXTRA_PARTNER_INFO"
        fun getLaunchIntent(context: Context, partnerInfo: PartnerInfo): Intent {
            val intent = Intent(context, PartnerInformationActivity::class.java)
            intent.putExtra(EXTRA_PARTNER_INFO, partnerInfo)
            return intent
        }
    }

    private lateinit var mData: PartnerInfo

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_information)

        mData = intent.getSerializableExtra(EXTRA_PARTNER_INFO) as PartnerInfo

        initViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {

        tv_name.text = mData.name
        tv_description.text = mData.desc
        tv_phone.text = mData.phone
        tv_uen.text = mData.uen
        tv_nea.text = mData.nea

        for (str in mData.offers) {
            val view = IndicatorTextView(this, str)
            ll_service_container.addView(view)
        }

        iv_close.setOnClickListener {
            onBackPressed()
        }
    }

    data class PartnerInfo(
            val name: String,
            val desc: String,
            val offers: List<String>,
            val phone: String,
            val uen: String,
            val nea: String
    ): Serializable
}
