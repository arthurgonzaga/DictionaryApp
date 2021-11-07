package com.arthurgonzaga.dictionary.presenter

import android.util.Log
import com.arthurgonzaga.dictionary.api.ApiService
import com.arthurgonzaga.dictionary.contract.MainContract
import com.arthurgonzaga.dictionary.model.WordResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
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
                object: SingleObserver<List<WordResponse>>{

                    override fun onSubscribe(d: Disposable) {}

                    override fun onSuccess(list: List<WordResponse>) {
                        view.setDescription(list[0].origin)
                    }

                    override fun onError(e: Throwable) { view.showErrorSnackBar(e.message) }
                }
            )
    }

}