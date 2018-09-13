package com.spgroup.spapp.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.spgroup.spapp.di.ViewModelKey
import com.spgroup.spapp.presentation.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(vmf: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(vm: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(vm: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PageViewModel::class)
    abstract fun bindPageViewModel(vm: PageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PartnerListingViewModel::class)
    abstract fun bindPartnerListingViewModel(vm: PartnerListingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PartnerDetailsViewModel::class)
    abstract fun bindPartnerDetailsViewModel(vm: PartnerDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PartnerInfoViewModel::class)
    abstract fun bindPartnerInfoViewModel(vm: PartnerInfoViewModel): ViewModel
}