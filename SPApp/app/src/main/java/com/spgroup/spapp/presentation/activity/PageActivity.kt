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
import com.spgroup.spapp.util.extension.inflate
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
    private lateinit var mPage: Page
    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        mType = intent.getStringExtra(EXTRA_TYPE)
        if (mType == null) throw IllegalArgumentException("Type is null")

        mPage = createDummy()

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
        tv_title.text = mPage.title
        when(mPage.code) {
            TYPE_ABOUT -> addAboutViews()

            TYPE_ACK -> addAckViews()
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

    private fun addAckViews() {
        val view = inflate(R.layout.layout_page_acknowledgement)
        view.run {
            val sectionText = mPage.findSection(SECTION_TEXT)
            val sectionLink = mPage.findSection(SECTION_LIST)
            if (sectionText == null || sectionLink == null) {
                throw IllegalArgumentException("Invalid data")
            }

            (sectionText as SectionLongText).run {
                tv_ack_content.text = this.text
            }

            (sectionLink as SectionList).run {
                tv_ack_title.text = this.title

                this.options.forEach {
                    val textView = IndicatorTextView(this@PageActivity, it)
                    ll_ack_option_container.addView(textView)
                }
            }
        }
        ll_container.addView(view)
    }

    private fun addAboutViews() {
        val view = inflate(R.layout.layout_page_about_us)
        view.run {
            val sectionText = mPage.findSection(SECTION_TEXT)
            val sectionLink = mPage.findSection(SECTION_LINK)
            if (sectionText == null || sectionLink == null) {
                throw IllegalArgumentException("Invalid data")
            }

            (sectionText as SectionLongText).run {
                tv_content.text = this.text
            }

            (sectionLink as SectionLink).run {
                tv_link_title.text = this.title
                tv_email.text = this.email
            }
        }
        ll_container.addView(view)
    }

    private fun createDummy(): Page {
        return when (mType) {
            TYPE_ABOUT -> createAbout()

            TYPE_ACK -> createAck()

            else -> throw IllegalArgumentException("Type $mType is invalid")
        }
    }

    private fun createAck(): Page {
        val section1 = SectionLongText(SECTION_TEXT, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi imperdiet sed metus at dapibus. Curabitur eget nisl euismod, aliquam felis sed, faucibus tortor. Duis nec ligula sit amet tortor finibus malesuada. Nullam id finibus eros. Sed magna metus, euismod a nisl nec, tincidunt facilisis justo. Etiam pulvinar et enim vel porttitor. Proin bibendum bibendum eros a convallis.")

        val options = listOf(
                "Ut gravida dictum lorem, id auctor dolor condimentum quis.",
                "Etiam malesuada eros quam, eu ornare sapien consequat at.",
                "In hac habitasse platea dictumst.",
                "Phasellus nec mollis odio.",
                "Etiam egestas luctus est vel pharetra, Fusce a egestas arcu"
        )

        val section2 = SectionList(SECTION_LIST, "On This App", options)
        return Page("Acknowledgement", TYPE_ACK, listOf(section1, section2))
    }

    private fun createAbout(): Page {
        val section1 = SectionLongText(SECTION_TEXT, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi imperdiet sed metus at dapibus. Curabitur eget nisl euismod, aliquam felis sed, faucibus tortor. Duis nec ligula sit amet tortor finibus malesuada. Nullam id finibus eros. Sed magna metus, euismod a nisl nec, tincidunt facilisis justo. Etiam pulvinar et enim vel porttitor. Proin bibendum bibendum eros a convallis.\r\n\r\nFusce a egestas arcu. Etiam malesuada eros quam, eu ornare sapien consequat at. Phasellus nec mollis odio. Etiam egestas luctus est vel pharetra. In hac habitasse platea dictumst. Nulla facilisi. Ut gravida dictum lorem, id auctor dolor condimentum quis.")
        val section2 = SectionLink(SECTION_LINK, "Feedback / Enquiries", "info@spgroup.com")

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
            override val type: String,
            val text: String
    ): Section(type)

    class SectionLink(
            override val type: String,
            val title: String,
            val email: String
    ): Section(type)

    class SectionList(
            override val type: String,
            val title: String,
            val options: List<String>
    ): Section(type)
}