package com.example.githubexplorer.ui.userdetail.di

import com.example.githubexplorer.ui.userdetail.UserDetailMapper
import com.example.githubexplorer.ui.userdetail.UserDetailViewModel
import com.example.githubexplorer.ui.userdetail.ghrepository.GithubRepositoryItemMapper
import com.example.githubexplorer.ui.userdetail.ghrepository.GithubRepositoryListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val userDetailMapperModule = module {
    factoryOf(::UserDetailMapper)
    factoryOf(::GithubRepositoryItemMapper)
}
private val userDetailViewModelModule = module {
    viewModel { (username: String) ->
        UserDetailViewModel(
            username = username,
            userRepository = get(),
            userDetailMapper = get(),
            dispatcherProvider = get(),
        )
    }
    viewModel { (username: String) ->
        GithubRepositoryListViewModel(
            username = username,
            repository = get(),
            itemMapper = get(),
            dispatcherProvider = get(),
        )
    }
}

val userDetailModule = userDetailViewModelModule + userDetailMapperModule