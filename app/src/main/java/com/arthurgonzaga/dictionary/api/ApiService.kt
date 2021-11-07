package com.arthurgonzaga.dictionary.api

import com.arthurgonzaga.dictionary.model.WordResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("entries/en/{word}/")
    fun searchWord(@Path("word") word: String): Single<List<WordResponse>>

    companion object {
        fun getInstance(): ApiService = Retrofit
            .Builder()
            .baseUrl("https://api.dictionaryapi.dev/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}