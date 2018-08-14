package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.activity_page.*
import kotlinx.android.synthetic.main.layout_about_us.view.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        mType = intent.getStringExtra(EXTRA_TYPE)
        if (mType == null) throw IllegalArgumentException("Type is null")

        mPage = createDummy()

        setUpViews()

    }

    private fun setUpViews() {
        tv_title.text = mPage.title
        when(mPage.code) {
            TYPE_ABOUT -> addAboutViews()
        }
    }

    private fun addAboutViews() {
        val view = inflate(R.layout.layout_about_us)
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

            else -> throw IllegalArgumentException("Type $mType is invalid")
        }
    }

    private fun createAbout(): Page {
        val section1 = SectionLongText(SECTION_TEXT, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi imperdiet sed metus at dapibus. Curabitur eget nisl euismod, aliquam felis sed, faucibus tortor. Duis nec ligula sit amet tortor finibus malesuada. Nullam id finibus eros. Sed magna metus, euismod a nisl nec, tincidunt facilisis justo. Etiam pulvinar et enim vel porttitor. Proin bibendum bibendum eros a convallis.\r\n\r\nFusce a egestas arcu. Etiam malesuada eros quam, eu ornare sapien consequat at. Phasellus nec mollis odio. Etiam egestas luctus est vel pharetra. In hac habitasse platea dictumst. Nulla facilisi. Ut gravida dictum lorem, id auctor dolor condimentum quis.")
        val section2 = SectionLink(SECTION_LINK, "Feedback / Enquiries", "info@spgroup.com")

        return Page(title = "About us", code = TYPE_ABOUT, sections = listOf(section1, section2))
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