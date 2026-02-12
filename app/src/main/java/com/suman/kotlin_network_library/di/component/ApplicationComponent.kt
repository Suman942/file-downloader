package com.suman.kotlin_network_library.di.component

import androidx.lifecycle.ViewModelProvider
import com.suman.kotlin_network_library.MyApplication
import com.suman.kotlin_network_library.di.module.ViewModelBindingModule
import com.suman.kotlin_network_library.di.module.ApplicationModule
import com.suman.kotlin_network_library.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelBindingModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: MyApplication)
    // 👇 THIS IS MANDATORY
    fun viewModelFactory(): ViewModelProvider.Factory
}