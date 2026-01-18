package com.suman.kotlin_network_library.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope