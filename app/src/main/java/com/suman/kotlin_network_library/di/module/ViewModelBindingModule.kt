package com.suman.kotlin_network_library.di.module

import androidx.lifecycle.ViewModel
import com.suman.kotlin_network_library.presentation.view_models.MainViewModel
import com.suman.kotlin_network_library.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindDownloadViewModel(
        viewModel: MainViewModel
    ): ViewModel
}