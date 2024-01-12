package com.example.resq.presentation.otp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.helper.UserDataInputStatus
import com.example.resq.model.struct.FcmTokenStruct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val otpCode = mutableStateOf("")
    val verificationId = mutableStateOf("")

    val resendCountdown = mutableStateOf(60)

    fun sendOtp(options: (auth: FirebaseAuth) -> PhoneAuthOptions) {
        repository.sendOtp(options = options)
    }

    fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccess: (UserDataInputStatus) -> Unit,
        onFailed: (Exception) -> Unit
    ) {
        repository.signInWithCredential(credential, onSuccess, onFailed)
    }

    fun handleFcmToken(
        onSuccess:() -> Unit
    ){
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