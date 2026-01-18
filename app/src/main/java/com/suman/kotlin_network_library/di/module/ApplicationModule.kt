package com.suman.kotlin_network_library.di.module

import android.content.Context
import com.suman.kotlin_network_library.MyApplication
import com.suman.kotlin_network_library.di.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: MyApplication) {

    @ApplicationContext
    @Provides
    fun providesApplicationContext(): Context {
        return application
    }
}