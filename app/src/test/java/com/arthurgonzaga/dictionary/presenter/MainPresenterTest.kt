package com.arthurgonzaga.dictionary.presenter

import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.contract.MainContract
import com.arthurgonzaga.dictionary.model.WordResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.lang.Exception
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins


class MainPresenterTest {

    private var view = mock(MainContract.View::class.java)
    private var service = mock(ApiService::class.java)
    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        reset(view)
        reset(service)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        presenter = MainPresenter(view, service)
    }

    @Test
    fun `should set the description when search a word`() {
        // Given
        val word = "cool"
        val origin = "this is the origin of the word cool"
        val response = Single.just(listOf(WordResponse(origin)))

        // When
        `when`(service.searchWord(word)).thenReturn(response)
        presenter.searchWord(word)

        // Then
        verify(view, times(1)).setDescription(origin)
    }

    @Test
    fun `should show a toast when an error occurs`(){
        // Given
        val word = "gdftdgvjghik"
        val exception = Exception("404 Not found")
        val response = Single.error<List<WordResponse>>(exception)

        // When
        `when`(service.searchWord(word)).thenReturn(response)
        presenter.searchWord(word)

        // Then
        verify(view, times(1)).showErrorSnackBar(exception.message)
    }


}