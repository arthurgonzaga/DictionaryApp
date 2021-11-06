package com.arthurgonzaga.dictionary.contract

import com.arthurgonzaga.dictionary.api.responses.WordResponse
import io.reactivex.rxjava3.core.Single

interface MainContract {

    interface View {

        fun setDescription(text: String)
        fun showErrorSnackBar(message: String?)
    }

    interface Presenter{

        fun searchWord(word: String)
    }
}