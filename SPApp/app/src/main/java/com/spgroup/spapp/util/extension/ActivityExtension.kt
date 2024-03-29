package com.spgroup.spapp.util.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.spgroup.spapp.R
import com.spgroup.spapp.SPApplication
import com.spgroup.spapp.presentation.activity.BaseActivity
import com.spgroup.spapp.presentation.activity.PartnerDetailsActivity
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity
import com.spgroup.spapp.presentation.fragment.BaseDialog
import com.spgroup.spapp.util.ConstUtils
import org.jetbrains.anko.longToast

inline fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)

inline fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)



inline fun <T : ViewModel> Fragment.obtainViewModelOfActivity(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this.activity!!, viewModelFactory).get(viewModelClass)

inline fun BaseActivity.openBrowser(url: String?) {
    if (url == null || url.isEmpty()) {
        longToast(getString(R.string.website_not_found))
        return
    }

    var fullUrl = url
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        fullUrl = "http://" + url
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
    startActivity(browserIntent)
}

inline fun Context.getPartnerDetailIntent(uen: String, partnerType: String): Intent {
    return when(partnerType) {

        ConstUtils.PARTNER_TYPE_CART -> PartnerDetailsActivity.getLaunchIntent(this, uen, true)

        ConstUtils.PARTNER_TYPE_INFO -> PartnerInformationActivity.getLaunchIntentForUnavailableData(this, uen)

        ConstUtils.PARTNER_TYPE_DETAIL_INFO -> PartnerDetailsActivity.getLaunchIntent(this, uen, false)

        else -> throw IllegalArgumentException("Invalid partner type: $partnerType with uen: $uen")

    }
}

inline fun Context.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun BaseActivity.showDialog(dialog: BaseDialog) {
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    val prevDialog = supportFragmentManager.findFragmentByTag(ConstUtils.TAG_DIALOG)
    if (prevDialog != null) {
        fragmentTransaction.remove(prevDialog)
    }
    dialog.show(fragmentTransaction, ConstUtils.TAG_DIALOG)
}

val Fragment.appInstance: SPApplication
    get() = activity?.application as SPApplication

val AppCompatActivity.appInstance: SPApplication
    get() = application as SPApplication

fun Context.openMailClient(chooserTitle: String = "", emailAddr: String = "", subject: String = "", body: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.setData(Uri.parse("mailto:"))
    intent.putExtra(Intent.EXTRA_EMAIL,  arrayOf(emailAddr))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, body)
    try {
        startActivity(Intent.createChooser(intent, chooserTitle))
    } catch (ex: ActivityNotFoundException) {
        longToast(R.string.error_no_mail_client_installed)
    }
}

fun Context.postDelay(delayInMillis: Long, action: () -> Unit) {
    android.os.Handler().postDelayed(object : Runnable {
        override fun run() {
            action.invoke()
        }
    }, delayInMillis)
}