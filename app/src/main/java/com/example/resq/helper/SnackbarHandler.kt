package com.example.resq.helper

import com.example.resq._showSnackbar
import com.example.resq._showSnackbarWithAction


class SnackbarHandler {
    companion object{
        fun showSnackbar (
            message: String
        ) {
            _showSnackbar(message)
        }

        fun showSnackbarWithAction(
            message: String,
            actionLabel:String = "Tutup",
            action:() -> Unit
        ){
            _showSnackbarWithAction(
                message,
                actionLabel,
                action
            )
        }
    }
}