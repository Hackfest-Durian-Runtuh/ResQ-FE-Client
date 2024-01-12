package com.example.resq.presentation.map

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resq.data.Repository
import com.example.resq.model.domain.general.PhoneNumberDomain
import com.example.resq.model.domain.map.MapEmergencyProviderDomain
import com.example.resq.model.domain.map.MapEmergencyTypeDomain
import com.example.resq.model.entity.FavoriteItemEntity
import com.example.resq.model.external.MapboxGeocodingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val useDummyLocation = mutableStateOf<Boolean?>(null)
    val copiedNumber = mutableStateOf("")

    val showRationaleDialog = mutableStateOf(false)
    val showPermissionWarningDialog = mutableStateOf(false)

    val userLong = mutableStateOf(112.6150769)
    val userLat = mutableStateOf(-7.9540899)

    val emTypes = mutableStateListOf<MapEmergencyTypeDomain>()
    val emProviders = mutableStateListOf<MapEmergencyProviderDomain>()
    val emPhoneNumbers = mutableStateListOf<PhoneNumberDomain>()

    val pickedEmergencyProvider = mutableStateOf<MapEmergencyProviderDomain?>(null)
    val pickedEmergencyProviderLocation = mutableStateOf<MapboxGeocodingResponse?>(null)
    val pickedEmTypeId = mutableStateOf("")

    val favoriteItems = mutableStateListOf<FavoriteItemEntity>()

    val availableTransportCount = mutableStateOf(0)

    fun getAllEmergencyProvider() {
        repository.getAllEmergencyProvider(
            onSuccess = {
                emProviders.clear()
                emProviders.addAll(
                    it.map { model ->
                        MapEmergencyProviderDomain(
                            em_pvd_id = model.em_pvd_id.toString(),
                            em_type = model.em_type.toString(),
                            name = model.name.toString(),
                            longitude = model.longitude.toString().toDouble(),
                            latitude = model.latitude.toString().toDouble()
                        )
                    }
                )
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    fun getAllEmergencyProviderByTypeId(
        emTypeId: String
    ) {
        repository.getAllEmergencyProviderByTypeId(
            emTypeId = emTypeId,
            onSuccess = {
                emProviders.clear()
                emProviders.addAll(
                    it.map { model ->
                        MapEmergencyProviderDomain(
                            em_pvd_id = model.em_pvd_id.toString(),
                            em_type = model.em_type.toString(),
                            name = model.name.toString(),
                            longitude = model.longitude.toString().toDouble(),
                            latitude = model.latitude.toString().toDouble()
                        )
                    }
                )
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    fun makeCallObjectInRealtimeDb(
        emPvdId: String,
        userLong: Double,
        userLat: Double
    ) {
        repository.makeCallObjectInRealtimeDb(
            emPvdId, userLong, userLat
        )
    }
    fun getContactByProviderId(
        emPvdId: String
    ) {
        repository.getContactByProviderId(
            emPvdId = emPvdId,
            onSuccess = {
                emPhoneNumbers.clear()
                emPhoneNumbers.addAll(
                    it.map { contact ->
                        PhoneNumberDomain(
                            phoneNumber = contact.number ?: "",
                            contactType = contact.contact_type ?: ""
                        )
                    }
                )
            },
            onFailed = {
                //TODO
            }
        )
    }

    fun getLocationByLongLat(long: Double, lat: Double) {
        viewModelScope.launch {
            repository.getLocationByLongLat(
                long,
                lat,
                onSuccess = {
                    pickedEmergencyProviderLocation.value = it
                },
                onFailed = {
                    Log.e("ERROR", it.toString())
                }
            )
        }
    }

    fun insertNewFavoriteItem(item: FavoriteItemEntity) =
        repository.insertNewFavoriteItem(item)

    fun deleteFavoriteItem(item: FavoriteItemEntity) =
        repository.deleteFavoriteItem(item)

    fun getFavoriteItems() = viewModelScope.launch(Dispatchers.IO) {
        val list = async {
            repository.getAllFavoriteItem()
        }

        favoriteItems.addAll(list.await())
    }

    fun getSingleTransportCount(
        emPvdId: String
    ) {
        repository.getSingleTransportCount(
            emPvdId = emPvdId,
            onSuccess = {
                availableTransportCount.value = it
            },
            onFailed = {
                //TODO
            }
        )
    }

    init {
        repository.getAllEmergencyType(
            onSuccess = {
                emTypes.clear()
                emTypes.addAll(
                    it.map { model ->
                        MapEmergencyTypeDomain(
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
    }
}