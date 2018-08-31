package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.di.Injection
import kotlinx.android.synthetic.main.activity_update.*
import org.jetbrains.anko.longToast

class UpdateActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, UpdateActivity::class.java)
            return intent
        }
    }

    private val mAppDataCache = Injection.provideAppDataCache()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        tv_update.setOnClickListener {
            openPlayStore()
        }
    }

    private fun openPlayStore() {
        val updateUrl = mAppDataCache.getTopLevelVariables().appLinkAndroid
        if (updateUrl.isEmpty()) {
            longToast(R.string.error_update_app_empty_url)
        } else {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)))
            } catch (anfe: android.content.ActivityNotFoundException) {
                longToast(R.string.error_update_app_activity_not_found)
            }
        }

    }
}