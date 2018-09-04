package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.activity_error.*

class FileNotFoundActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, FileNotFoundActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        setUpViews()
    }

    private fun setUpViews() {
        iv_error_icon.setImageResource(R.drawable.error_file_not_found)
        tv_title.setText(R.string.file_not_found_title)
        tv_description.setText(R.string.file_not_found_desc)
        tv_back.isGone = true
        tv_refresh.setText(R.string.back)
        tv_refresh.setOnClickListener {
            onBackPressed()
        }
    }
}