package com.suman.kotlin_network_library.di.module

import androidx.lifecycle.ViewModelProvider
import com.suman.kotlin_network_library.di.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideDownloadViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory {
        return factory
    }
}
