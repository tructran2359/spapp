package com.spgroup.spapp.util.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.spgroup.spapp.R
import org.jetbrains.anko.longToast

inline fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)

inline fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)



inline fun <T : ViewModel> Fragment.obtainViewModelOfActivity(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this.activity!!, viewModelFactory).get(viewModelClass)

inline fun Context.openBrowser(url: String?) {
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