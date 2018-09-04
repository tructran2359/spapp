package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.isOnline
import kotlinx.android.synthetic.main.activity_error.*
import org.jetbrains.anko.longToast

class NoInternetActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context, pendingIntent: Intent?, requestCode: Int): Intent {
            val intent = Intent(context, NoInternetActivity::class.java)
            intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent)
            intent.putExtra(EXTRA_REQUEST_CODE, requestCode)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        setUpViews()
    }

    private fun setUpViews() {
        tv_back.setOnClickListener {
            onBackPressed()
        }

        tv_refresh.setOnClickListener {
            if (isOnline()) {
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                longToast(R.string.no_internet_connection_error_message)
            }
        }
    }
}