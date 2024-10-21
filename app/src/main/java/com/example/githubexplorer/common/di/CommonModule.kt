package com.example.githubexplorer.common.di

import com.example.githubexplorer.common.formatter.NumberFormatter
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val commonModule = module {
    factoryOf(::NumberFormatter)
}