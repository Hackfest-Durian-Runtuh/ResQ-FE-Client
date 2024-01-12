package com.example.resq.presentation.biodata_form

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiodataFormViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){
    val fullname = mutableStateOf("")
    val nickname = mutableStateOf("")
    val expandBloodType = mutableStateOf(false)
    val bloodType = mutableStateOf("")
    val weight = mutableStateOf("")
    val height = mutableStateOf("")

    val nik = mutableStateOf("")
    val tempatLahir = mutableStateOf("")
    val tanggalLahir = mutableStateOf("")
    val namaAsuransi = mutableStateOf("")
    val noAsuransi = mutableStateOf("")
}