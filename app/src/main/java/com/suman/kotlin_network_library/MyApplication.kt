package com.suman.kotlin_network_library

import android.app.Application
import com.suman.kotlin_network_library.di.component.ApplicationComponent
import com.suman.kotlin_network_library.di.component.DaggerApplicationComponent
import com.suman.kotlin_network_library.di.module.ApplicationModule
import com.suman.network_library.Downloader

class MyApplication: Application() {
//    lateinit var downloader:Downloader
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        getDependencies()
//        downloader= Downloader.create(this)
    }

    private fun getDependencies(){
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}