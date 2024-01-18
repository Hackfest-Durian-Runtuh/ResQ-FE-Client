package com.example.resq.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resq.data.Repository
import com.example.resq.helper.UserDataInputStatus
import com.example.resq.model.struct.FcmTokenStruct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun precheck(
        onLoginChecked: (isLogin: Boolean) -> Unit,
        onUserDataInputStatusCheck: (String, UserDataInputStatus) -> Unit
    ) {
        val loginStatus = repository.isLogin()
        onLoginChecked(loginStatus)

        repository.uid()?.let { uid ->
            Log.e("COY", "HALOOO")
            Log.e("UID", uid)
            if (loginStatus) {
                repository.checkUserInputDataStatus(
                    uid = uid,
                    onSuccess = { phoneNumber, status ->
                            onUserDataInputStatusCheck(phoneNumber, status)
                    },
                    onFailed = {
                        //TODO
                    }
                )
            }
        }
    }

    fun handleFcmToken(
        onSuccess: () -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val fcm = FirebaseMessaging.getInstance()

        fcm.token.addOnSuccessListener { token ->
            firestore
                .collection("fcm_token")
                .document(auth.currentUser?.uid ?: "")
                .set(
                    FcmTokenStruct(
                        uid = auth.currentUser?.uid ?: "",
                        token = token
                    )
                )
                .addOnSuccessListener {
                    onSuccess()
                }
        }
    }
}