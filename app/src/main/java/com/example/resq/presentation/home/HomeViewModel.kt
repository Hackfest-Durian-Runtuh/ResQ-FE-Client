package com.example.resq.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resq.data.Repository
import com.example.resq.model.domain.home.HomeEmergencyTypeDomain
import com.example.resq.model.entity.FavoriteItemEntity
import com.example.resq.model.external.MapboxGeocodingResponse
import com.example.resq.model.struct.CallModel
import com.example.resq.model.struct.EmergencyProviderModel
import com.example.resq.model.struct.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val copiedNumber = mutableStateOf("")
    val emTypes = mutableStateListOf<HomeEmergencyTypeDomain>()
    val favoritePhoneProviders = mutableStateListOf<FavoriteItemEntity>()
    val availableTransportCountMaps = mutableStateOf(mapOf<String, Int>())
    val userInfo = mutableStateOf<UserModel?>(null)
    val lastCall = mutableStateOf<CallModel?>(null)
    val lastCallEmProvider = mutableStateOf<EmergencyProviderModel?>(null)
    val lastCallLocationResponse = mutableStateOf<MapboxGeocodingResponse?>(null)
    val callStatusMap = mutableMapOf<String, String>()

    fun deleteFavoriteItem(item: FavoriteItemEntity) = repository.deleteFavoriteItem(item)

    fun getAllFavorites() = viewModelScope.launch(Dispatchers.IO) {
        val list = async {
            repository.getAllFavoriteItem()
        }

        favoritePhoneProviders.addAll(list.await())
    }

    fun getMultipleTransportCount(
        emPvdIds: List<String>
    ) {
        repository.getMultipleTransportCount(
            emPvdIds = emPvdIds,
            onSuccess = {
                availableTransportCountMaps.value = it
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    fun getEmergencyProviderById(
        emPvdId: String
    ) {
        repository.getEmergencyProviderById(
            emPvdId = emPvdId,
            onSuccess = {
                lastCallEmProvider.value = it
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    fun getLocationFromLongLat(
        long: Double,
        lat: Double
    ) {
        viewModelScope.launch {
            repository.getLocationByLongLat(
                longitude = long,
                latitude = lat,
                onSuccess = {
                    lastCallLocationResponse.value = it
                },
                onFailed = {
                    Log.e("ERROR", it.toString())
                }
            )
        }
    }

    init {
        repository.getAllEmergencyType(
            onSuccess = {
                emTypes.clear()
                emTypes.addAll(
                    it.map { model ->
                        HomeEmergencyTypeDomain(
                            emTypeId = model.emTypeId ?: "",
                            word = model.word ?: ""
                        )
                    }
                )
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )

        repository.getUserInfo(
            onSuccess = {
                userInfo.value = it
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )

        repository.listenEmCallSnapshot(
            onListened = {
                if(it.size > 0){
                    lastCall.value = it.last()
                }
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )

        repository.getAllCallStatus(
            onSuccess = {
                it.forEach { item ->
                    callStatusMap.put(
                        key = item.em_call_status_id,
                        value = item.word
                    )
                }
            },
            onFailed = {
                Log.e(
                    "ERROR",
                    it.toString()
                )
            }
        )
    }
}