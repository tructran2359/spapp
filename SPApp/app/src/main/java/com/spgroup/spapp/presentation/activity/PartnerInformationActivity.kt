package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation
    private var mAnimIsRunning = false

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_information)

        mData = intent.getSerializableExtra(EXTRA_PARTNER_INFO) as PartnerInfo

        initAnimation()
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
        tv_offer_tittle.text = mData.offerTitle

        for (str in mData.offers) {
            val view = IndicatorTextView(this, str)
            ll_service_container.addView(view)
        }

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
            val desc: String,
            val offerTitle: String,
            val offers: List<String>,
            val phone: String,
            val uen: String,
            val nea: String
    ): Serializable
}
