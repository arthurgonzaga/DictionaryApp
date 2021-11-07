package com.arthurgonzaga.dictionary.contract

interface MainContract {

    interface View {

        fun setDescription(text: String)
        fun showErrorSnackBar(message: String?)
    }

    interface Presenter{

        fun searchWord(word: String)
    }
}