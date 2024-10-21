package com.example.githubexplorer.ui.userlist.di

import com.example.githubexplorer.ui.userlist.UserListViewModel
import com.example.githubexplorer.ui.userlist.UserMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val mapperModule = module {
    factoryOf(::UserMapper)
}

private val userViewModelModule = module {
    factoryOf(::UserListViewModel)
}

val userModule = listOf(
    userViewModelModule,
    mapperModule
)