package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.isOnline
import kotlinx.android.synthetic.main.activity_error.*

class NoInternetActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context, pendingIntent: Intent?, requestCode: Int): Intent {
            val intent = Intent(context, NoInternetActivity::class.java)
            intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent)
            intent.putExtra(EXTRA_REQUEST_CODE, requestCode)
            return intent
        }

        fun getLaunchIntentForSplash(context: Context): Intent {
            val intent = Intent(context, NoInternetActivity::class.java)
            intent.putExtra(EXTRA_FOR_SPLASH, true)
            return intent
        }
    }

    private var mIsForSplash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        mIsForSplash = intent.getBooleanExtra(EXTRA_FOR_SPLASH, false)

        setUpViews()
    }

    private fun setUpViews() {
        tv_back.isGone = mIsForSplash
        tv_back.setOnClickListener {
            onBackPressed()
        }

        tv_refresh.setOnClickListener {
            if (isOnline()) {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}