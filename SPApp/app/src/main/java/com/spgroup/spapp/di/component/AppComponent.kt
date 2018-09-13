package com.spgroup.spapp.di.component

import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.di.module.*
import com.spgroup.spapp.presentation.SPApplication
import com.spgroup.spapp.presentation.activity.SplashActivity
import com.spgroup.spapp.presentation.activity.UpdateActivity
import dagger.Component

@Component(modules = [
    AppModule::class,
    RepoModule::class,
    NetworkModule::class,
    MapperModule::class,
    GsonModule::class,
    UsecaseModule::class,
    ViewModelModule::class
])
@ApplicationScoped
interface AppComponent {

    fun inject(spApplication: SPApplication)

    fun inject(splashActivity: SplashActivity)

    fun inject(updateActivity: UpdateActivity)

}