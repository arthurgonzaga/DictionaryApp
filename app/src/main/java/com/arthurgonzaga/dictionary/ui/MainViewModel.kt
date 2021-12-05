package com.arthurgonzaga.dictionary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.repository.MainRepository
import com.arthurgonzaga.dictionary.repository.MainRepositoryImpl
import com.arthurgonzaga.dictionary.utils.AsyncResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(
    var repository: MainRepository = MainRepositoryImpl(ApiService.getInstance())
): ViewModel() {

    private val disposables = CompositeDisposable()


    val description: LiveData<String> get() = _description
    private val _description = MutableLiveData<String>()

    val error: LiveData<String>  get() =  _error
    private val _error = MutableLiveData<String>()


    fun searchWord(word: String) {
        repository
            .getDescription(word)
            .subscribeOn(Schedulers.io() )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onDescriptionResult)
            .let(disposables::add)
    }

    private fun onDescriptionResult(result : AsyncResult<String>){
        when(result){
            is AsyncResult.Success -> {
                _description.value = result.value ?: ""
            }
            is AsyncResult.Failure -> {
                _error.value = result.error.message
            }
        }
    }
}