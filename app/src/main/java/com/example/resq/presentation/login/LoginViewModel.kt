package com.example.resq.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val phoneNumber = mutableStateOf("")
}