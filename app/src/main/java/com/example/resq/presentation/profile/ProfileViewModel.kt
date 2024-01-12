package com.example.resq.presentation.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.model.struct.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel() {
    val userInfo = mutableStateOf<UserModel?>(null)

    fun logout(){
        repository.logout()
    }

    init {
        repository.getUserInfo(
            onSuccess = {
                userInfo.value = it
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }
}