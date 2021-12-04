package com.arthurgonzaga.dictionary.utils

sealed class AsyncResult<out T : Any> {
    data class Success<out T : Any>(val value: T? = null) : AsyncResult<T>()
    data class Failure(val error: Throwable) : AsyncResult<Nothing>()
    object Loading : AsyncResult<Nothing>()
}

val <T> T.exhaustive: T get() = this