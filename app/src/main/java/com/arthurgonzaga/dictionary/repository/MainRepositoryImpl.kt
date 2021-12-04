package com.arthurgonzaga.dictionary.repository

import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.utils.AsyncResult
import io.reactivex.rxjava3.core.Single

class MainRepositoryImpl(
    private val service: ApiService
): MainRepository {

    override fun getDescription(word: String): Single<out AsyncResult<String>> {
        return service.searchWord(word).flatMap {
            Single.just<AsyncResult<String>>(AsyncResult.Success(it[0].origin))
        }.onErrorReturn {
            AsyncResult.Failure(it)
        }
    }

}