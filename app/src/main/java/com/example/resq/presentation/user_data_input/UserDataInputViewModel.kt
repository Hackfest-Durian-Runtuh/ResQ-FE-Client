package com.example.resq.presentation.user_data_input

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDataInputViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel() {
    val nama = mutableStateOf("")
    val nik = mutableStateOf("")

    fun saveUserDataInput(
        phoneNumber:String,
        name:String,
        nik:String,
        onSuccess:() -> Unit,
        onFailed: (Exception) -> Unit
    ){
        repository.saveUserDataInput(phoneNumber, name, nik, onSuccess, onFailed)
    }
}