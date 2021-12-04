package com.arthurgonzaga.dictionary.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.model.WordResponse
import com.arthurgonzaga.dictionary.repository.MainRepositoryImpl
import com.arthurgonzaga.dictionary.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val service = mock(ApiService::class.java)

    private val repository = MainRepositoryImpl(service)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setErrorHandler { e: Throwable? -> println(e?.message)}
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Mockito.reset(service)
    }


    @Test
    fun `should set the description when search a word`() {
        // Given
        val word = "cool"
        val origin = "this is the origin of the word cool"
        val response = Single.just(listOf(WordResponse(origin)))

        // When
        Mockito.`when`(service.searchWord(word)).thenReturn(response)
        viewModel.searchWord(word)

        // Then
        val description = viewModel.description.getOrAwaitValue()
        assertThat(description).isEqualTo(origin)
    }

    @Test
    fun `should show a toast when an error occurs`(){
        // Given
        val word = "gdftdgvjghik"
        val exception = Exception("404 Not found")
        val response = Single.error<List<WordResponse>>(exception)

        // When
        `when`(service.searchWord(word)).thenReturn(response)
        viewModel.searchWord(word)

        // Then
        val error = viewModel.error.getOrAwaitValue()
        assertThat(error).isEqualTo(exception.message)
    }

}