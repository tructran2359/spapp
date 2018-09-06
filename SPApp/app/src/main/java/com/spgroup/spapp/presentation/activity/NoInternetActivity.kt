package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.isOnline
import com.spgroup.spapp.util.extension.updateMainButtonEnable
import kotlinx.android.synthetic.main.activity_error.*

class NoInternetActivity: BaseErrorActivity() {

    companion object {

        const val WHAT_TIME_OUT = 1

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
    private lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mIsForSplash = intent.getBooleanExtra(EXTRA_FOR_SPLASH, false)

        setUpViews()
        setUpHandler()
    }

    override fun onDestroy() {
        mHandler.removeMessages(WHAT_TIME_OUT)
        super.onDestroy()
    }

    private fun setUpHandler() {
        mHandler = object: Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                msg?.run {
                    when (what) {
                        WHAT_TIME_OUT -> {
                            iv_error_icon.setImageResource(R.drawable.error_no_connection)
                            tv_refresh.updateMainButtonEnable(true)
                            if (isOnline()) {
                                onBackToOnline()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpViews() {
        tv_back.isGone = mIsForSplash
        tv_back.setOnClickListener {
            onBackPressed()
        }

        tv_refresh.setOnClickListener {
            if (isOnline()) {
                onBackToOnline()
            } else {
                it.updateMainButtonEnable(false)
                iv_error_icon.setImageResource(R.drawable.no_connection_ani)
                mHandler.sendEmptyMessageDelayed(WHAT_TIME_OUT, 3000)
            }
        }
    }

    private fun onBackToOnline() {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}