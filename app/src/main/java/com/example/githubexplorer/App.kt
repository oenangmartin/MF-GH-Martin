package com.example.githubexplorer

import android.app.Application
import com.example.githubexplorer.common.di.commonModule
import com.example.githubexplorer.coroutine.di.coroutineModule
import com.example.githubexplorer.repository.di.repositoryModule
import com.example.githubexplorer.ui.userdetail.di.userDetailModule
import com.example.githubexplorer.ui.userlist.di.userModule
import com.example.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                networkModule +
                        repositoryModule +
                        userModule +
                        userDetailModule +
                        coroutineModule +
                        commonModule
            )
        }
    }
}