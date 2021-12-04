package com.arthurgonzaga.dictionary.repository

import com.arthurgonzaga.dictionary.utils.AsyncResult
import io.reactivex.rxjava3.core.Single

interface MainRepository {

    fun getDescription(word: String): Single<out AsyncResult<String>>
}