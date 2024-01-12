package com.example.resq.presentation.call_detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.model.struct.CallModel
import com.example.resq.model.struct.EmergencyProviderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallDetailViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel(){
    val call = mutableStateOf<CallModel?>(null)
    val emProvider = mutableStateOf<EmergencyProviderModel?>(null)
    val statusMap = mutableMapOf<String, String>()
    val emProviderTypeMap = mutableMapOf<String, String>()
    val long = mutableStateOf(.0)
    val lat = mutableStateOf(.0)
    val realtimeStatus = mutableStateOf("...")

    fun getCallInfoFromId(
        id:String,
        onListened:(CallModel) -> Unit,
        onFailed:(Exception) -> Unit
    ){
        repository.listenEmCallSnapshotById(
            emCallId = id,
            onListened = {
                onListened(it)
            },
            onFailed = {
                onFailed(it)
            }
        )
    }

    fun getEmProvider(
        emPvdId:String
    ){
        repository.getEmergencyProviderById(
            emPvdId = emPvdId,
            onSuccess = {
                emProvider.value = it
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    init {
        repository.getAllCallStatus(
            onSuccess = {
                it.forEach { item ->
                    statusMap[item.em_call_status_id] = item.word
                }
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )

        repository.getAllEmergencyType(
            onSuccess = {
                it.forEach {
                    emProviderTypeMap[it.emTypeId ?: "-"] = it.word ?: "-"
                }
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }
}