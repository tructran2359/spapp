package com.spgroup.spapp.util.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

inline fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)

inline fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)



inline fun <T : ViewModel> Fragment.obtainViewModelOfActivity(viewModelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory?) =
        ViewModelProviders.of(this.activity!!, viewModelFactory).get(viewModelClass)