package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.Extension.setOnGlobalLayoutListener
import kotlinx.android.synthetic.main.activity_partner_details.*

class PartnerDetailsActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, PartnerDetailsActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_details)

        setupViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun setupViews() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        rl_hero_section.setOnGlobalLayoutListener {
            val width = rl_hero_section.width
            val height = (width * 9f / 16f).toInt()
            val layoutParams = rl_hero_section.layoutParams
            layoutParams.height = height
            rl_hero_section.layoutParams = layoutParams
        }

//        rl_hero_section.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                val width = rl_hero_section.width
//                val height = (width * 9f / 16f).toInt()
//                val layoutParams = rl_hero_section.layoutParams
//                layoutParams.height = height
//                rl_hero_section.layoutParams = layoutParams
//                rl_hero_section.viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })
    }

}
