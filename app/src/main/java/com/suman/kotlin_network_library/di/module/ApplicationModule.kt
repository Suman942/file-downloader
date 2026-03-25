package com.suman.kotlin_network_library.di.module

import android.content.Context
import com.suman.kotlin_network_library.MyApplication
import com.suman.kotlin_network_library.di.ApplicationContext
import com.suman.downloader.Downloader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApplication) {

    @ApplicationContext
    @Provides
    fun providesApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesDownloader(@ApplicationContext context: Context): Downloader = Downloader.create(context)
}