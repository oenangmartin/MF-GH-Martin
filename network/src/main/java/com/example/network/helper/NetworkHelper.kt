package com.example.network.helper

import com.example.network.exception.CommonError
import com.example.network.exception.NoInternetException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Result.failure(NoInternetException)
                is HttpException -> {
                    val code = throwable.code()
                    Result.failure(CommonError(code, throwable.message()))
                }
                else -> {
                    Result.failure(throwable)
                }
            }
        }
    }
}