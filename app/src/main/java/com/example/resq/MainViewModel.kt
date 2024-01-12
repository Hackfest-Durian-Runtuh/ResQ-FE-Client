package com.example.resq

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.navhost.NavRoutes

class MainViewModel : ViewModel() {
    val loading = mutableStateOf(false)
    val currentRoute = mutableStateOf(NavRoutes.SPLASH.name)
    val showBottomBar = mutableStateOf(false)
    val backClicked = mutableStateOf(false)
}