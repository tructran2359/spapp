package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.setLayoutParamsSizeFromDimens
import kotlinx.android.synthetic.main.activity_error.*

class ApiErrorActivity: BaseErrorActivity() {

    companion object {

        fun getLaunchIntent(context: Context) = Intent(context, ApiErrorActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViews()
    }

    private fun setUpViews() {
        iv_error_icon.setLayoutParamsSizeFromDimens(
                R.dimen.error_screen_api_error_icon_size_width,
                R.dimen.error_screen_api_error_icon_size_height
        )
        iv_error_icon.setImageResource(R.drawable.error_api)
        tv_title.setText(R.string.api_error_title)
        tv_description.setText(R.string.api_error_desc)
        tv_refresh.setText(R.string.back)
        tv_back.isGone = true
        tv_refresh.setOnClickListener {
            onBackPressed()
        }
    }

    private fun goBackWithResultOk() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}