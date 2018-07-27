package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.activity_menu_detail.*

class MenuDetailActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, MenuDetailActivity::class.java)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_detail)

        pdf_view
                .fromAsset("dummy.pdf")
                .defaultPage(0)
                .swipeVertical(true)
                .load()

        action_bar.setOnBackPress {
            this@MenuDetailActivity.onBackPressed()
        }

    }
}