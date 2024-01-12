package com.example.resq.helper

import com.example.resq.mainViewModel


object LoadingHandler{
    fun loading() {
        mainViewModel.loading.value = true
    }

    fun dismiss() {
        mainViewModel.loading.value = false
    }
}