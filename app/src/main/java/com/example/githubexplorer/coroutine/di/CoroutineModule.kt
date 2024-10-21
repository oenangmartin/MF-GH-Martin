package com.example.githubexplorer.coroutine.di

import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.githubexplorer.coroutine.DispatcherProviderImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coroutineModule = module {
    factoryOf(::DispatcherProviderImpl) { bind<DispatcherProvider>() }
}