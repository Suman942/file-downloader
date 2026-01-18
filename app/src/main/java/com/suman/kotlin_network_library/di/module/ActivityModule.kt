package com.suman.kotlin_network_library.di.module

import android.app.Activity
import android.content.Context
import com.suman.kotlin_network_library.DownloadAdapter
import com.suman.kotlin_network_library.di.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @ActivityScope
    @Provides
    fun providesActivityContext(): Context{
        return activity
    }

    @ActivityScope
    @Provides
    fun providesDownloadAdapter(): DownloadAdapter = DownloadAdapter()
}