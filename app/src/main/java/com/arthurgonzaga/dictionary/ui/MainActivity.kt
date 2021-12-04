package com.arthurgonzaga.dictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.arthurgonzaga.dictionary.R
import com.arthurgonzaga.dictionary.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEditTextListener()
        addObservers()
    }

    private fun setupEditTextListener(){
        binding.wordEditText.textChanges()
            .skipInitialValue()
            .doOnNext { binding.descriptionTextView.text = "" }
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .filter { it.toString().isNotBlank() }
            .doOnNext { text ->
                viewModel.searchWord(text.toString())
            }
            .subscribe()
    }

    private fun addObservers(){
        viewModel.run {
            description.observe(this@MainActivity){ description ->
                setDescription(description)
            }
            error.observe(this@MainActivity){ errorMessage ->
                showErrorSnackBar(errorMessage)
            }
        }
    }

    private fun setDescription(text: String) {
        binding.descriptionTextView.text = text
    }

    private fun showErrorSnackBar(message: String?) {
        val textId = if(message?.contains("404") == true) R.string.word_not_found else R.string.something_wrong
        Snackbar.make(binding.root, textId, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val DEBOUNCE_TIMEOUT = 700L
    }
}