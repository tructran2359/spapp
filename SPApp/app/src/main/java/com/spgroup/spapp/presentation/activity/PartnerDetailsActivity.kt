package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.PartnerImagesAdapter
import com.spgroup.spapp.presentation.fragment.CartPartnerDetailFragment
import com.spgroup.spapp.presentation.fragment.DetailInfoPartnerDetailFragment
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_partner_details.*
import kotlin.math.max

class PartnerDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    ///////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////

    companion object {

        fun getLaunchIntent(context: Context, partnerUEN: String, isCart: Boolean): Intent {
            val intent = Intent(context, PartnerDetailsActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_PARTNER_UEN, partnerUEN)
            intent.putExtra(ConstUtils.EXTRA_IS_CART, isCart)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mImageAdapter: PartnerImagesAdapter
//    lateinit var mCategoryAdapter: CategoryPagerAdapter
    lateinit var mAnimationAppear: Animation
    lateinit var mAnimationDisappear: Animation
    private var mActionBarHeight: Int = 0
    private var mInitTextSize = 0f
    private var mPromotionBarHeight = 0
    private lateinit var mViewModel: PartnerDetailsViewModel
    private var mIsCart = true

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_details)

        mActionBarHeight = getDimensionPixelSize(R.dimen.action_bar_height)
        mIsCart = intent.getBooleanExtra(ConstUtils.EXTRA_IS_CART, true)

        initAnimations()

        setUpViews()

        setupViewModel()
    }

    private fun setupViewModel() {
        val partnerUEN = intent.getStringExtra(ConstUtils.EXTRA_PARTNER_UEN)
        doLogD("Partner", "onCreate partner: ${partnerUEN ?: "null"}")
        // This is demo for using ViewModel
        mViewModel = obtainViewModel(PartnerDetailsViewModel::class.java, ViewModelFactory.getInstance())
                .apply { this.partnerUEN = partnerUEN }
        with(mViewModel) {

            partnerDetails.observe(this@PartnerDetailsActivity, Observer {
                it?.let {

                    tv_partner_name.text = it.name
                    tv_promotion.text = it.promo
                    val hasPromoBar = it.promo == null || it.promo.isEmpty()
                    ll_promotion_bar.isGone = hasPromoBar
                    mPromotionBarHeight = if (hasPromoBar) 0 else getDimensionPixelSize(R.dimen.promotion_bar_height)
                    setUpBanners(it.banners)
                }
            })

            if (mIsCart) {
                selectedCount.observe(this@PartnerDetailsActivity, Observer {
                    doLogD(msg = "Selected $it item")
                    it?.let {
                        // Temporarily hide this line coz updating total count textview is not in this task
                        //                    btn_summary.setCount(it)
                        if (it != 0 && ll_summary_section.visibility == View.GONE) {
                            showSummaryButton(true)
                        } else if (it == 0 && ll_summary_section.visibility == View.VISIBLE) {
                            showSummaryButton(false)
                        }
                    }
                })
            }

            error.observe(this@PartnerDetailsActivity, Observer {
                // do something with error
                doLogE(msg = "Error: ${it?.message}")
            })

            loadServices()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // AppBarLayout
    ///////////////////////////////////////////////////////////////////////////

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        if (appBarLayout == null) return
        val currentScroll = appBarLayout.bottom
        val mMaxScroll = appBarLayout.totalScrollRange
        val percentage = currentScroll.toFloat() / (mMaxScroll).toFloat()

        val layoutParam = rl_top_button_container.layoutParams
        layoutParam.height = max(mActionBarHeight, currentScroll - mPromotionBarHeight)
        if (currentScroll <= mActionBarHeight) {
            v_background_color.alpha = 1f
            fl_info_container.visibility = View.GONE
        } else {
            v_background_color.alpha = 1f - percentage
            fl_info_container.visibility = View.VISIBLE
            fl_info_container.alpha = percentage
        }


        rl_top_button_container.layoutParams = layoutParam
        val scaledTextSize = mInitTextSize * max(percentage, 0.7f)
        tv_partner_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaledTextSize)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initAnimations() {
        mAnimationAppear = loadAnimation(R.anim.anim_slide_up)
        mAnimationAppear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                updateVisibility(true)
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })

        mAnimationDisappear = loadAnimation(R.anim.anim_slide_down)
        mAnimationDisappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                updateVisibility(false)
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    private fun setUpViews() {
        mInitTextSize = tv_partner_name.textSize

        setUpHeroSection()

        setUpFormSection()

        setUpSummarySection()
    }

    private fun setUpFormSection() {
        val fragment = if (mIsCart) {
            CartPartnerDetailFragment()
        } else {
            DetailInfoPartnerDetailFragment()
        }

        supportFragmentManager.beginTransaction().add(R.id.fl_forms_section, fragment).commit()
    }

    private fun setUpBanners(urls: List<String>?) {
        urls?.let {
            mImageAdapter = PartnerImagesAdapter(supportFragmentManager, urls)
            pager_images.offscreenPageLimit = 3
            pager_images.adapter = mImageAdapter

            pager_indicator.setViewPager(pager_images)
        }
    }

    private fun setUpHeroSection() {
        fl_back_container.setOnClickListener {
            onBackPressed()
        }

        fl_info_container.setOnClickListener {
            val partnerInfo = mViewModel.getPartnerInfoModel()
            partnerInfo?.let {
                startActivity(PartnerInformationActivity.getLaunchIntentForAvailableData(this@PartnerDetailsActivity, it))
            }
        }

        rl_hero_section.setOnGlobalLayoutListener {
            val width = rl_hero_section.width
            val height = (width * 9f / 16f).toInt()
            val layoutParams = rl_hero_section.layoutParams
            val layoutParamsMask = rl_top_button_container.layoutParams
            layoutParams.height = height
            layoutParamsMask.height = height
            rl_hero_section.layoutParams = layoutParams
            rl_top_button_container.layoutParams = layoutParamsMask
        }

        appbar.addOnOffsetChangedListener(this)
    }

    private fun setUpSummarySection() {
        ll_summary_section.isGone = mIsCart
        btn_summary.isGone = !mIsCart
        tv_visit_website.isGone = mIsCart

        btn_summary.setOnClickListener {
            val intent = OrderSummaryActivity.getLaunchIntent(this)
            startActivity(intent)
        }
        btn_summary.setPrice(0f)

        tv_visit_website.setOnClickListener {
            val url = mViewModel.partnerDetails.value?.website
            openBrowser(url)
        }
    }

    private fun showSummaryButton(show: Boolean) {
        val animation = if (show) mAnimationAppear else mAnimationDisappear
        ll_summary_section.startAnimation(animation)
    }

    private fun updateVisibility(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        ll_summary_section.visibility = visibility

        val params = fl_forms_section.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = if (show) getDimensionPixelSize(R.dimen.bottom_button_total_height) else 0
        fl_forms_section.layoutParams = params
    }

}
