package com.suman.kotlin_network_library.di.component

import com.suman.kotlin_network_library.MyApplication
import com.suman.kotlin_network_library.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(application: MyApplication)
}