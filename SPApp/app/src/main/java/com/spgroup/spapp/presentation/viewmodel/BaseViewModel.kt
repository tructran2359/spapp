package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val disposeBag = CompositeDisposable()
    val error = MutableLiveData<Throwable>()

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }
}