package com.arthurgonzaga.dictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.arthurgonzaga.dictionary.R
import com.arthurgonzaga.dictionary.contract.MainContract
import com.arthurgonzaga.dictionary.databinding.ActivityMainBinding
import com.arthurgonzaga.dictionary.presenter.MainPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var binding: ActivityMainBinding
    private val presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setupEditTextListener()
    }

    private fun setupEditTextListener(){
        binding.wordEditText.textChanges()
            .skipInitialValue()
            .doOnNext { binding.descriptionTextView.text = "" }
            .debounce(1, TimeUnit.SECONDS)
            .filter { it.toString().isNotBlank() }
            .doOnNext { text-> presenter.searchWord(text.toString()) }
            .subscribe()
    }

    override fun setDescription(text: String) {
        binding.descriptionTextView.text = text
    }

    override fun showErrorSnackBar(message: String?) {
        val textId = if(message?.contains("404") == true) R.string.word_not_found else R.string.something_wrong
        Snackbar.make(binding.root, textId, Snackbar.LENGTH_SHORT).show()
    }

}