package com.arthurgonzaga.dictionary.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.arthurgonzaga.dictionary.R
import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.model.WordResponse
import com.arthurgonzaga.dictionary.repository.MainRepositoryImpl
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private var wordToBeTyped = "cool"
    private var origin = "this is the origin of the word cool"

    private var service = Mockito.mock(ApiService::class.java)

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        activityRule.activity.viewModel.repository = MainRepositoryImpl(service)
    }

    @Test
    fun should_set_the_description_when_user_type_a_word() {
        // Given
        wordToBeTyped = "cool"
        val response = Single.just(listOf(WordResponse(origin)))

        // When
        `when`(service.searchWord(wordToBeTyped)).thenReturn(response)
        onView(withId(R.id.word_edit_text)).perform(typeText(wordToBeTyped))

        // Then
        Thread.sleep(MainActivity.DEBOUNCE_TIMEOUT + 5)
        onView(withId(R.id.description_text_view)).check(matches(withText(origin)))
    }

    @Test
    fun should_show_a_toast_when_not_find_the_word() {
        // Given
        wordToBeTyped = "gdftdgvjghik"
        val exception = Exception("404 Not found")
        val response = Single.error<List<WordResponse>>(exception)

        // When
        `when`(service.searchWord(wordToBeTyped)).thenReturn(response)
        onView(withId(R.id.word_edit_text)).perform(typeText(wordToBeTyped))

        // Then
        Thread.sleep(MainActivity.DEBOUNCE_TIMEOUT + 20)
        onView(withText(activityRule.activity.getString(R.string.word_not_found))).check(matches(isDisplayed()))
    }

    @Test
    fun should_show_a_toast_when_some_error_happen() {
        // Given
        wordToBeTyped = "cool"
        val exception = Exception("Random error")
        val response = Single.error<List<WordResponse>>(exception)

        // When
        `when`(service.searchWord(wordToBeTyped)).thenReturn(response)
        onView(withId(R.id.word_edit_text)).perform(typeText(wordToBeTyped))

        // Then
        Thread.sleep(MainActivity.DEBOUNCE_TIMEOUT + 20)
        onView(withText(activityRule.activity.getString(R.string.something_wrong))).check(matches(isDisplayed()))
    }
}