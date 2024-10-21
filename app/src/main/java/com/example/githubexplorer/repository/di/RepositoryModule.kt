package com.example.githubexplorer.repository.di

import com.example.githubexplorer.repository.GithubRepoRepository
import com.example.githubexplorer.repository.GithubRepoRepositoryImpl
import com.example.githubexplorer.repository.UserRepository
import com.example.githubexplorer.repository.UserRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::UserRepositoryImpl) { bind<UserRepository>() }
    factoryOf(::GithubRepoRepositoryImpl) { bind<GithubRepoRepository>() }
}