package com.spgroup.spapp.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.di.ViewModelKey
import com.spgroup.spapp.domain.usecase.GetInitialDataUsecase
import com.spgroup.spapp.manager.AppDataCache
import com.spgroup.spapp.presentation.viewmodel.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
class ViewModelModule {

    @Provides
    @ApplicationScoped
    fun provideViewModelFactory(providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return providers[modelClass]?.let { provider ->
                    provider.get() as T
                } ?: throw RuntimeException("$modelClass is not found !!!")
            }
        }
    }

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideSplashViewModel(getInitialDataUsecase: GetInitialDataUsecase, appDataCache: AppDataCache): ViewModel {
        return SplashViewModel(getInitialDataUsecase, appDataCache)
    }
}