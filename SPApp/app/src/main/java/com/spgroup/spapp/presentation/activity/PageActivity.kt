package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelPage
import com.spgroup.spapp.domain.model.TopLevelPageSectionLink
import com.spgroup.spapp.domain.model.TopLevelPageSectionList
import com.spgroup.spapp.domain.model.TopLevelPageSectionLongText
import com.spgroup.spapp.presentation.view.IndicatorTextView
import com.spgroup.spapp.presentation.viewmodel.PageViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.inflate
import com.spgroup.spapp.util.extension.obtainViewModel
import kotlinx.android.synthetic.main.activity_page.*
import kotlinx.android.synthetic.main.layout_page_about_us.view.*
import kotlinx.android.synthetic.main.layout_page_acknowledgement.view.*

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
//    private lateinit var mPage: Page
    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation
    private lateinit var mViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        mType = intent.getStringExtra(EXTRA_TYPE)
        if (mType == null) throw IllegalArgumentException("Type is null")

//        mPage = createDummy()

        mViewModel = obtainViewModel(PageViewModel::class.java, ViewModelFactory.getInstance())
        mViewModel.run {
            page.observe(this@PageActivity, Observer {
                it?.let {
                    tv_title.text = it.title
                    when(it.code) {
                        TYPE_ABOUT -> addAboutViews(it)

                        TYPE_ACK -> addAckViews(it)

                        TYPE_TNC -> addTncViews()
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
                    updateCloseIcon(false)
                } else if (scrollY == 0) {
                    // Reach Top
                    updateCloseIcon(true)
                }
            }
        })
    }

    private fun addTncViews() {
//        val view = inflate(R.layout.layout_page_tnc)
//        view.run {
//            val sectionTnc = mPage.sections[0] as SectionLongText
//            val sectionCopyright = mPage.sections[1] as SectionLongText
//
//            tv_tnc.text = sectionTnc.text
//            tv_copyright_title.text = sectionCopyright.title
//            tv_copyright.text = sectionCopyright.text
//        }
//        ll_container.addView(view)
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

                        it.options.forEach {
                            val textView = IndicatorTextView(this@PageActivity, it)
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
                        tv_email.text = it.email
                    }

                    is TopLevelPageSectionLongText -> {
                        tv_content.text = it.text
                    }
                }
            }

        }
        ll_container.addView(view)
    }

    private fun createDummy(): Page {
        return when (mType) {
            TYPE_ABOUT -> createAbout()

            TYPE_ACK -> createAck()

            TYPE_TNC -> createTnc()

            else -> throw IllegalArgumentException("Type $mType is invalid")
        }
    }

    private fun createTnc(): Page {
        val sectionTnC = SectionLongText("PLEASE READ THESE TERMS AND CONDITIONS OF USE (“TERMS AND CONDITIONS”) CAREFULLY. BY ACCESSING THIS WEBSITE AND/OR USING THE ONLINE SERVICES, YOU AGREE TO BE BOUND BY THE FOLLOWING TERMS AND CONDITIONS. IF YOU DO NOT ACCEPT ANY OF THESE TERMS AND CONDITIONS, YOU MUST IMMEDIATELY DISCONTINUE YOUR ACCESS OF THIS WEBSITE AND/OR USE OF THE ONLINE SERVICES.")
        val sectionCopyright = SectionLongText("Except as otherwise expressly stated herein, the copyright and all other intellectual property in the contents of this Website (including, but not limited to, all design, text, sound recordings, images or links) are the property of Singapore Power Limited and/or its subsidiaries and/or their respective subsidiaries (together the \"SP Group\"). As such, they may not be reproduced, transmitted, published, performed, broadcast, stored, adapted, distributed, displayed, licensed, altered, hyperlinked or otherwise used in whole or in part in any manner without the prior written consent of the SP Group. Save and except with the SP Group's prior written consent, you may not insert a hyperlink to this Website or any part thereof on any other website or \"mirror\" or frame this Website, any part thereof, or any information or materials contained in this Website on any other server, website or webpage.\n" +
                "\n" +
                "All trade marks, service marks and logos used in this Website are the property of the SP Group and/or the respective third party proprietors identified in this Website. No licence or right is granted and your access to this Website and/or use of the online services should not be construed as granting, by implication, estoppel or otherwise, any license or right to use any trade marks, service marks or logos appearing on the Website without the prior written consent of the SP Group or the relevant third party proprietor thereof. Save and except with the SP Group's prior written consent, no such trade mark, service mark or logo may be used as a hyperlink or to mark any hyperlink to any SP Group member's site or any other site.",
                "Copyright and Trademark Notices")

        return Page(title = "Terms & Conditions", code = TYPE_TNC, sections = listOf(sectionTnC, sectionCopyright))
    }

    private fun createAck(): Page {
        val section1 = SectionLongText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi imperdiet sed metus at dapibus. Curabitur eget nisl euismod, aliquam felis sed, faucibus tortor. Duis nec ligula sit amet tortor finibus malesuada. Nullam id finibus eros. Sed magna metus, euismod a nisl nec, tincidunt facilisis justo. Etiam pulvinar et enim vel porttitor. Proin bibendum bibendum eros a convallis.")

        val options = listOf(
                "Ut gravida dictum lorem, id auctor dolor condimentum quis.",
                "Etiam malesuada eros quam, eu ornare sapien consequat at.",
                "In hac habitasse platea dictumst.",
                "Phasellus nec mollis odio.",
                "Etiam egestas luctus est vel pharetra, Fusce a egestas arcu"
        )

        val section2 = SectionList("On This App", options)
        return Page("Acknowledgement", TYPE_ACK, listOf(section1, section2))
    }

    private fun createAbout(): Page {
        val section1 = SectionLongText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi imperdiet sed metus at dapibus. Curabitur eget nisl euismod, aliquam felis sed, faucibus tortor. Duis nec ligula sit amet tortor finibus malesuada. Nullam id finibus eros. Sed magna metus, euismod a nisl nec, tincidunt facilisis justo. Etiam pulvinar et enim vel porttitor. Proin bibendum bibendum eros a convallis.\r\n\r\nFusce a egestas arcu. Etiam malesuada eros quam, eu ornare sapien consequat at. Phasellus nec mollis odio. Etiam egestas luctus est vel pharetra. In hac habitasse platea dictumst. Nulla facilisi. Ut gravida dictum lorem, id auctor dolor condimentum quis.")
        val section2 = SectionLink("Feedback / Enquiries", "info@spgroup.com")

        return Page(title = "About us", code = TYPE_ABOUT, sections = listOf(section1, section2))
    }

    private fun updateCloseIcon(show: Boolean) {
        if (show && iv_close.visibility == View.GONE) {
            iv_close.startAnimation(mAnimAppear)
        } else if (!show && iv_close.visibility == View.VISIBLE) {
            iv_close.startAnimation(mAnimDisappear)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class
    ///////////////////////////////////////////////////////////////////////////

    data class Page(
            val title: String,
            val code: String,
            val sections: List<Section>
    ) {
        fun findSection(type: String): Section? {
            sections.forEach {
                if (it.type.equals(type)) return it
            }

            return null
        }
    }

    open class Section (open val type: String)

    class SectionLongText(
            val text: String,
            val title: String = ""
    ): Section(SECTION_TEXT)

    class SectionLink(
            val title: String,
            val email: String
    ): Section(SECTION_LINK)

    class SectionList(
            val title: String,
            val options: List<String>
    ): Section(SECTION_LIST)
}