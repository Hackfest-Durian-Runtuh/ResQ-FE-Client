package com.example.resq.presentation.history

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.model.struct.CallModel
import com.example.resq.model.struct.EmergencyProviderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val listCall = mutableStateListOf<CallModel>()
    val providerNameMap = mutableMapOf<String, EmergencyProviderModel>()
    val statusMap = mutableMapOf<String, String>()

    init {
        repository.listenEmCallSnapshot(
            onListened = {
                listCall.clear()
                listCall.addAll(it.reversed())

                it.forEach { model ->
                    repository.getEmergencyProviderById(
                        model.em_pvd_id ?: "",
                        onSuccess = {
                            providerNameMap[it.em_pvd_id ?: ""] = it
                        },
                        onFailed = {
                            Log.e("ERROR", it.toString())
                        }
                    )
                }
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )

        repository.getAllCallStatus(
            onSuccess = {
                it.forEach { status ->
                    statusMap[status.em_call_status_id] = status.word
                }
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }
}