package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.PartnerInfoViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_partner_information.*
import org.jetbrains.anko.longToast
import java.io.Serializable

class PartnerInformationActivity : BaseActivity() {

    companion object {

        const val EXTRA_PARTNER_INFO = "EXTRA_PARTNER_INFO"
        const val EXTRA_DATA_AVAILABLE = "EXTRA_DATA_AVAILABLE"
        const val EXTRA_UEN = "EXTRA_UEN"
        fun getLaunchIntentForAvailableData(context: Context, partnerInfo: PartnerInfo): Intent {
            val intent = Intent(context, PartnerInformationActivity::class.java)
            intent.putExtra(EXTRA_PARTNER_INFO, partnerInfo)
            intent.putExtra(EXTRA_DATA_AVAILABLE, true)
            return intent
        }

        fun getLaunchIntentForUnavailableData(context: Context, uen: String): Intent {
            val intent = Intent(context, PartnerInformationActivity::class.java)
            intent.putExtra(EXTRA_UEN, uen)
            intent.putExtra(EXTRA_DATA_AVAILABLE, false)
            return intent
        }
    }

//    private lateinit var mData: PartnerInfo
    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation
    private lateinit var mViewModel: PartnerInfoViewModel
    private var mAnimIsRunning = false

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_information)

        mViewModel = obtainViewModel(PartnerInfoViewModel::class.java, ViewModelFactory.getInstance())
        mViewModel.mPartnerInfo.observe(this, Observer {
            it?.let {
                showData(it)
            }
        })
        val dataAvailable = intent.getBooleanExtra(EXTRA_DATA_AVAILABLE, false)
        rl_bottom_button_container.isGone = dataAvailable
        v_button_shadow.isGone = dataAvailable

        if (dataAvailable) {
            val data = intent.getSerializableExtra(EXTRA_PARTNER_INFO) as PartnerInfo
            mViewModel.showData(data)
        } else {
            val uen = intent.getStringExtra(EXTRA_UEN)
            mViewModel.loadData(uen)
        }

        initAnimation()
        initViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {

        iv_close.setOnClickListener {
            onBackPressed()
        }

        scroll_view.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(view: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                if (scrollY > oldScrollY) {
                    // Scroll down
                    if (!mAnimIsRunning) {
                        updateCloseIcon(false)
                    }
                } else if (scrollY == 0) {
                    // Reach Top
                    updateCloseIcon(true)
                }
            }
        })
    }

    private fun showData(data: PartnerInfo) {
        data.run {
            tv_name.text = name
            tv_description.text = desc

            tv_phone.text = phone
            ll_phone_container.isGoneWithText(phone)

            tv_uen.text = uen
            ll_uen_container.isGoneWithText(uen)

            tv_nea.text = nea
            ll_nea_container.isGoneWithText(nea)

            tv_email.text = email
            ll_email_container.isGoneWithText(email)

            // hide `Business Information` title if all info are not available
            tv_business_info.isGone = (ll_phone_container.isGone && ll_uen_container.isGone && ll_nea_container.isGone)


            tv_offer_tittle.text = offeringTitle
            ll_offering.addIndicatorText(data.offering)
            // check if empty list or all member of list is empty to hide view
            var needToHideOfferingViews = true
            data.offering?.forEach {
                if (!it.isEmpty()) {
                    needToHideOfferingViews = false
                    return@forEach
                }
            }
            tv_offer_tittle.isGone = needToHideOfferingViews
            ll_offering.isGone = needToHideOfferingViews

            // Hide TnC if data is not available
            if (tnc == null || tnc.isEmpty()) {
                tv_merchant_tnc.isGone = true
            } else {

                // Setup TnC link from server
                tv_merchant_tnc.setUpClickableUnderlineSpan(R.string.merchant_tnc_with_format, R.string.merchant_tnc_underline_text) {
                    if (data.tnc != null && !data.tnc.isEmpty()) {
                        val intent = PdfActivity.getLaunchIntent(
                                this@PartnerInformationActivity,
                                data.name,
                                data.tnc)
                        startActivityWithCheckingInternet(intent)
                    } else {
                        longToast(R.string.not_available)
                    }
                }
            }

            tv_visit_website.setOnClickListener {
                openBrowser(website)
            }
        }

    }

    private fun initAnimation() {
        mAnimAppear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        mAnimAppear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                iv_close.visibility = View.VISIBLE
                mAnimIsRunning = false
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
        mAnimDisappear = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out)
        mAnimDisappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                iv_close.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    private fun updateCloseIcon(show: Boolean) {
        if (show && iv_close.visibility == View.GONE) {
            iv_close.startAnimation(mAnimAppear)
        } else if (!show && iv_close.visibility == View.VISIBLE && !mAnimIsRunning) {
            mAnimIsRunning = true
            iv_close.startAnimation(mAnimDisappear)
        }
    }

    data class PartnerInfo(
            val name: String,
            val desc: String?,
            val offeringTitle: String?,
            val offering: List<String>?,
            val phone: String?,
            val uen: String?,
            val nea: String?,
            val website: String?,
            val tnc: String?,
            val email: String?
    ): Serializable
}
