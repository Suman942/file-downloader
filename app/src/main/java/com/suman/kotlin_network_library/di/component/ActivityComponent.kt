package com.suman.kotlin_network_library.di.component

import androidx.lifecycle.ViewModelProvider
import com.suman.kotlin_network_library.presentation.ui.MainActivity
import com.suman.kotlin_network_library.di.ActivityScope
import com.suman.kotlin_network_library.di.module.ActivityModule
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [ApplicationComponent::class])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)

}