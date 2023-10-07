package com.aktepetugce.noteapp.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.toResult(isLoading: Boolean = true): Flow<Result<T>> {
    return map<T, Result<T>> {
        Result.Success(it)
    }
        .onStart {
            if (isLoading) {
                emit(Result.Loading)
            }
        }
        .catch { exception ->
            emit(Result.Error(exception.message ?: exception.toString()))
        }
}