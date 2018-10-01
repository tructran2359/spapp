package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.*
import com.spgroup.spapp.presentation.view.IndicatorTextView
import com.spgroup.spapp.presentation.viewmodel.PageViewModel
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_page.*
import kotlinx.android.synthetic.main.layout_page_about_us.view.*
import kotlinx.android.synthetic.main.layout_page_acknowledgement.view.*
import javax.inject.Inject

class PageActivity: BaseActivity() {

    companion object {
        const val EXTRA_TYPE = "extra-type"
        const val TYPE_ABOUT = "about"
        const val TYPE_ACK = "acknowledgement"
        const val TYPE_TNC = "tnc"
        const val SECTION_TEXT = "page_long_text"
        const val SECTION_LINK = "page_link"
        const val SECTION_LIST = "page_list"

        fun getLaunchIntent(context: Context, type: String): Intent {
            val intent = Intent(context, PageActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            return intent
        }
    }

    private var mType: String? = null
    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation
    private lateinit var mViewModel: PageViewModel
    private var mAnimIsRunning = false

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_page)

        mType = intent.getStringExtra(EXTRA_TYPE)
        if (mType == null) throw IllegalArgumentException("Type is null")

//        mPage = createDummy()

        mViewModel = obtainViewModel(PageViewModel::class.java, vmFactory)
        mViewModel.run {
            page.observe(this@PageActivity, Observer {
                it?.let {topLevelPage ->
                    tv_title.text = topLevelPage.title
                    when(topLevelPage.code) {
                        TYPE_ABOUT -> addAboutViews(topLevelPage)

                        TYPE_ACK -> addAckViews(topLevelPage)

                        TYPE_TNC -> addTncViews(topLevelPage)
                    }
                }

            })

            loadPageFromCache(mType!!)
        }

        initAnimation()

        setUpViews()

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

    private fun setUpViews() {

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

    private fun addTncViews(page: TopLevelPage) {
        page.sections.forEachIndexed { index, section ->
            val view = when (section) {
                is TopLevelPageSectionLongText -> createTncText(section)
                is TopLevelPageSectionSubtitle -> createTncSubTitle(section)
                else -> null
            }

            if (view != null) {
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.topMargin = if (index == 0) 0 else getDimensionPixelSize(R.dimen.common_vert_medium_sub)
                view.layoutParams = layoutParams
                ll_container.addView(view)
            }
        }
    }

    private fun createTncSubTitle(section: TopLevelPageSectionSubtitle): View {
        val view = inflate(R.layout.layout_tnc_subtitle) as TextView
        view.text = section.title
        return view
    }

    private fun createTncText(section: TopLevelPageSectionLongText): View {
        val view = inflate(R.layout.layout_tnc_text) as TextView
        view.text = section.text
        return view
    }

    private fun addAckViews(page: TopLevelPage) {
        val view = inflate(R.layout.layout_page_acknowledgement)
        view.run {
            page.sections.forEach {
                when (it) {
                    is TopLevelPageSectionLongText -> {
                        tv_ack_content.text = it.text
                    }
                    is TopLevelPageSectionList -> {
                        tv_ack_title.text = it.title

                        it.options.forEach {text ->
                            val textView = IndicatorTextView(this@PageActivity, text)
                            ll_ack_option_container.addView(textView)
                        }
                    }
                }
            }
        }
        ll_container.addView(view)
    }

    private fun addAboutViews(page: TopLevelPage) {
        val view = inflate(R.layout.layout_page_about_us)
        view.run {
            page.sections.forEach {
                when (it) {
                    is TopLevelPageSectionLink -> {
                        tv_link_title.text = it.title
                        val email = it.email
                        tv_email.text = email
                        ll_email_container.isGoneWithText(email)
                        ll_email_container.setOnClickListener { _ ->
                            openMailClient(getString(R.string.chooser_mail_client), email)
                        }
                    }

                    is TopLevelPageSectionLongText -> {
                        tv_content.text = it.text
                    }
                }
            }

        }
        ll_container.addView(view)
    }

    private fun updateCloseIcon(show: Boolean) {
        if (show && iv_close.visibility == View.GONE) {
            iv_close.startAnimation(mAnimAppear)
        } else if (!show && iv_close.visibility == View.VISIBLE && !mAnimIsRunning) {
            mAnimIsRunning = true
            iv_close.startAnimation(mAnimDisappear)
        }
    }
}