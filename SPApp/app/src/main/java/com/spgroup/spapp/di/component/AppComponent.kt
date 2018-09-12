package com.spgroup.spapp.di.component

import com.spgroup.spapp.di.ApplicationScoped
import com.spgroup.spapp.di.module.*
import dagger.Component

@Component(modules = [
    AppModule::class,
    RepoModule::class,
    NetworkModule::class,
    MapperModule::class,
    GsonModule::class
])
@ApplicationScoped
interface AppComponent