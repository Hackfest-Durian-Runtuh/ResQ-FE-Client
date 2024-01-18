package com.example.resq.presentation.biodata

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.model.struct.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiodataViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val biodataList = mutableStateListOf<UserModel>()
    val uid = repository.uid()

    init {
        repository.getUserInfo(
            onSuccess = {
                biodataList.add(it)
            },
            onFailed = {}
        )

        repository.getBiodataListExceptMe(
            onSuccess = {
                biodataList.clear()
                biodataList.addAll(it)
            },
            onFailed = {}
        )
    }
}