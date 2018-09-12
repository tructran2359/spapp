package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.di.ApplicationScoped
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScoped
class AppViewModelFactory @Inject constructor(private val providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.let { provider ->
            provider.get() as T
        } ?: throw RuntimeException("$modelClass is not found !!!")
    }

}