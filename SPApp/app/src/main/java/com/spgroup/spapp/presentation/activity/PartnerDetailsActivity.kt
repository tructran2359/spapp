package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.animation.Animation
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.adapter.PartnerImagesAdapter
import com.spgroup.spapp.presentation.fragment.CartPartnerDetailFragment
import com.spgroup.spapp.presentation.fragment.DetailInfoPartnerDetailFragment
import com.spgroup.spapp.presentation.fragment.PartnerImageFragment
import com.spgroup.spapp.presentation.fragment.SpAlertDialog
import com.spgroup.spapp.presentation.viewmodel.ISelectedService
import com.spgroup.spapp.presentation.viewmodel.PartnerDetailsViewModel
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_partner_details.*
import javax.inject.Inject

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
    lateinit var mAnimationAppear: Animation
    lateinit var mAnimationDisappear: Animation
    private var mActionBarHeight: Int = 0
    private var mInitTextSize = 0f
    private var mPromotionBarHeight = 0
    private var mIsCart = true

    private lateinit var mViewModel: PartnerDetailsViewModel
    @Inject lateinit var vmFactory: ViewModelProvider.Factory

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_partner_details)

        mActionBarHeight = getDimensionPixelSize(R.dimen.action_bar_height)
        mIsCart = intent.getBooleanExtra(ConstUtils.EXTRA_IS_CART, true)

        initAnimations()

        setUpViews()

        setupViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CUSTOMISE && resultCode == Activity.RESULT_OK) {
            data?.run {
                val customiseDisplayData = data.getSerializableExtra(CustomiseNewActivity.EXTRA_DISPLAY_DATA) as CustomiseDisplayData
                mViewModel.addComplexSelectedServiceItem(customiseDisplayData)
            }
        } else if (requestCode == RC_ORDER_SUMMARY) {
            if (resultCode == Activity.RESULT_OK) {
                doLogD("EmptyResques", "onActivityResult call refresh")
                mViewModel.clearAllSelectedService()
            } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
                val selectedServiceMap
                        = data.getSerializableExtra(OrderSummaryActivity.EXTRA_SERVICE_MAP) as HashMap<String, MutableList<ISelectedService>>
                mViewModel.updateSelectedService(selectedServiceMap)
            }
        }
    }

    override fun onBackPressed() {
        val totalCount = mViewModel.selectedCount.value ?: 0
        if (totalCount == 0) {
            super.onBackPressed()
        } else {
            showAlertPopup()
        }
    }

    private fun setupViewModel() {
        val partnerUEN = intent.getStringExtra(ConstUtils.EXTRA_PARTNER_UEN)
//        doLogD("Partner", "onCreate partner: ${partnerUEN ?: "null"}")
        // This is demo for using ViewModel
        mViewModel = obtainViewModel(PartnerDetailsViewModel::class.java, vmFactory)
                .apply { this.partnerUEN = partnerUEN }
        with(mViewModel) {

            partnerDetails.observe(this@PartnerDetailsActivity, Observer {
                it?.let {

                    doLogD("EmptyResques", "partner detail update UI")

                    tv_hero_section_partner_name.text = it.name
                    tv_partner_name.text = it.name
                    tv_partner_name.requestLayout()
//                    doLogD("PartnerName", "Set name: ${it.name} textSize: ${tv_partner_name.textSize} visi: ${tv_partner_name.visibility} height ${tv_partner_name.height}")

                    tv_promotion.text = it.promo
                    val hasPromoBar = it.promo == null || it.promo.isEmpty()
                    ll_promotion_bar.isGone = hasPromoBar
                    mPromotionBarHeight = if (hasPromoBar) 0 else getDimensionPixelSize(R.dimen.promotion_bar_height)
                    setUpBanners(it.banners, it.categoryId)
//                    setUpBanners(listOf("123","234","345"), it.categoryId) // simulate all wrong url
//                    setUpBanners(listOf(), it.categoryId) // simulate empty list
                }
            })

            if (mIsCart) {
                selectedCount.observe(this@PartnerDetailsActivity, Observer {
                    doLogD(msg = "Selected $it item")
                    it?.let {
                        btn_summary.setCount(it)
                        if (it != 0 && ll_summary_section.visibility == View.GONE) {
                            showSummaryButton(true)
                        } else if (it == 0 && ll_summary_section.visibility == View.VISIBLE) {
                            showSummaryButton(false)
                        }
                    }
                })

                estimatedPrice.observe(this@PartnerDetailsActivity, Observer {
                    it?.let {
                        btn_summary.setEstPrice(it)
                    }
                })
            }

            error.observe(this@PartnerDetailsActivity, Observer {
                // do something with error
                doLogE(msg = "Error: ${it?.message}")

                // Start error activity and finish so BACK on error act will back to partner listing
                startActivity(ApiErrorActivity.getLaunchIntent(this@PartnerDetailsActivity))
                finish()
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
        val percentage = (currentScroll - mActionBarHeight).toFloat() / (mMaxScroll).toFloat()

        if (currentScroll <= mActionBarHeight) {
            v_background_color.alpha = 1f
            tv_partner_name.alpha = 1f
            fl_info_container.visibility = View.INVISIBLE
        } else {
            v_background_color.alpha = 1f - percentage
            tv_partner_name.alpha = 1f - percentage
            fl_info_container.visibility = View.VISIBLE
            fl_info_container.alpha = percentage
        }
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

    private fun setUpBanners(urls: List<String>?, cateId: String) {
        if (urls == null || urls.isEmpty()) {
            mImageAdapter = PartnerImagesAdapter(
                    supportFragmentManager,
                    listOf(PartnerImageFragment.PLACEHOLDER),
                    cateId
            )
            pager_images.adapter = mImageAdapter
            pager_indicator.isGone = true
            return
        }

        mImageAdapter = PartnerImagesAdapter(supportFragmentManager, urls, cateId)
        pager_images.offscreenPageLimit = 3
        pager_images.adapter = mImageAdapter

        pager_indicator.setViewPager(pager_images)
        pager_indicator.isGone = urls.size < 2
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
            layoutParams.height = height
            rl_hero_section.layoutParams = layoutParams
        }

        appbar.addOnOffsetChangedListener(this)
    }

    private fun setUpSummarySection() {
        ll_summary_section.isGone = mIsCart
        btn_summary.isGone = !mIsCart
        tv_visit_website.isGone = mIsCart

        btn_summary.setOnClickListener {
            mViewModel.run {

                if (estimatedPrice.value != null &&  partnerDetails.value != null) {
//                    val estPrice = -1 //Simulate value smaller than minimum order amount
                    val estPrice = estimatedPrice.value ?: 0f
                    val minimumPrice = partnerDetails.value?.getMinimumOrderValue() ?: 0f
                    if (estPrice < minimumPrice) {
                        showMinimumOrderPopup(minimumPrice)
                    } else {
                        moveToOrderSummary()
                    }
                }
            }
        }
        btn_summary.setPrice(0f)

        tv_visit_website.setOnClickListener {
            val url = mViewModel.partnerDetails.value?.website
            openBrowser(url)
        }
    }

    private fun showMinimumOrderPopup(minimumPrice: Float) {
        var title: String
        var desc: String
        var positiveText: String
        var negativeText: String?

        val hasCheckboxSelected = mViewModel.hasCheckboxSelected()
        if (hasCheckboxSelected) {
            title = getString(R.string.dialog_min_order_checkbox_title_with_format, minimumPrice.formatPrice())
            desc = getString(R.string.dialog_min_order_checkbox_desc)
            positiveText = getString(R.string.ok)
            negativeText = null
        } else {
            title = getString(R.string.dialog_min_order_title, minimumPrice.formatPrice())
            desc = getString(R.string.dialog_min_order_notice)
            positiveText = getString(R.string.text_continue)
            negativeText = getString(R.string.add_more_services)
        }

        var dialog = SpAlertDialog.getInstance(title, desc, positiveText, negativeText)
        dialog.setPositiveAction {
            dialog.dismiss()
            moveToOrderSummary()
        }
        dialog.setNegativeAction {
            dialog.dismiss()
        }
        showDialog(dialog)
    }

    private fun moveToOrderSummary() {
        mViewModel.partnerDetails.value?.run {
            val intent = OrderSummaryActivity.getLaunchIntent(
                    context = this@PartnerDetailsActivity,
                    mapSelectedServices = mViewModel.getMapSelectedServices(),
                    partnerDetails = this
            )
            startActivityForResult(intent, RC_ORDER_SUMMARY)
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

    private fun showAlertPopup() {
        val title = getString(R.string.partner_details_alert_title)
        val desc = getString(R.string.partner_details_alert_desc)
        val positiveText = getString(R.string.text_continue)
        val negativeText = getString(R.string.cancel)

        val dialog = SpAlertDialog.getInstance(title, desc, positiveText, negativeText)
        dialog.setPositiveAction {
            dialog.dismiss()
            finish()
        }

        dialog.setNegativeAction {
            dialog.dismiss()
        }

        showDialog(dialog)
    }

}
