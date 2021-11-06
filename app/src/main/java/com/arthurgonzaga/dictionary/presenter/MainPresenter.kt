package com.arthurgonzaga.dictionary.presenter

import android.util.Log
import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.contract.MainContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainPresenter(
    private val view: MainContract.View,
    private val service: ApiService = ApiService.getInstance(),
): MainContract.Presenter {

    override fun searchWord(word: String){
        service.searchWord(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list -> view.setDescription(list[0].origin) },
                { view.showErrorSnackBar(it.message) }
            )
    }

}