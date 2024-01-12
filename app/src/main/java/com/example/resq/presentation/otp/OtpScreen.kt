package com.example.resq.presentation.otp

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.resq.R
import com.example.resq.helper.LoadingHandler
import com.example.resq.helper.SnackbarHandler
import com.example.resq.helper.UserDataInputStatus
import com.example.resq.navhost.NavRoutes
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    phoneNumber: String,
    navController: NavController
) {
    val viewModel = hiltViewModel<OtpViewModel>()
    val context = LocalContext.current
    val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            viewModel.otpCode.value = p0.smsCode ?: ""
            LoadingHandler.loading()
            viewModel.signInWithCredential(
                credential = p0,
                onSuccess = {
                    viewModel.handleFcmToken {
                        when (it) {
                            UserDataInputStatus.INPUTTED -> {
                                navController.navigate(NavRoutes.BERANDA.name) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }

                            UserDataInputStatus.HAVE_NOT_INPUTTED -> {
                                navController.navigate("${NavRoutes.USER_DATA_INPUT.name}/$phoneNumber") {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }

                    LoadingHandler.dismiss()
                },
                onFailed = {
                    SnackbarHandler.showSnackbar("ERROR: ${it.message}")
                    LoadingHandler.dismiss()
                }
            )
        }

        override fun onVerificationFailed(p0: FirebaseException) {}

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            viewModel.verificationId.value = verificationId
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.sendOtp(
            options = { auth ->
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(context as Activity) // Activity (for callback binding)
                    .setCallbacks(callback) // OnVerificationStateChangedCallbacks
                    .build()
            }
        )
    }

    LaunchedEffect(key1 = viewModel.resendCountdown.value) {
        if (viewModel.resendCountdown.value > 0) {
            delay(1000)
            viewModel.resendCountdown.value -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 128.dp),
            model = R.drawable.ic_splash,
            contentDescription = ""
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Masukkan Kode OTP", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.otpCode.value,
                onValueChange = {
                    viewModel.otpCode.value = it
                },
                label = {
                    Text(text = "Kode OTP")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 128.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    LoadingHandler.loading()

                    val credential = PhoneAuthProvider.getCredential(
                        viewModel.verificationId.value,
                        viewModel.otpCode.value
                    )

                    viewModel.signInWithCredential(
                        credential = credential,
                        onSuccess = {
                            when (it) {
                                UserDataInputStatus.INPUTTED -> {
                                    navController.navigate(NavRoutes.BERANDA.name) {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }

                                UserDataInputStatus.HAVE_NOT_INPUTTED -> {
                                    navController.navigate("${NavRoutes.USER_DATA_INPUT.name}/$phoneNumber") {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            LoadingHandler.dismiss()
                        },
                        onFailed = {
//                            SnackbarHandler.showSnackbar("ERROR: ${it.message}")
                            LoadingHandler.dismiss()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                enabled = viewModel.otpCode.value.isNotEmpty()
            ) {
                Text(text = "Verifikasi OTP", color = MaterialTheme.colorScheme.onPrimary)
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                text = "Tunggu ${viewModel.resendCountdown.value} detik lagi untuk kirim ulang"
            )

            Text(
                modifier = Modifier.clickable(
                    onClick = {
                        viewModel.sendOtp(
                            options = { auth ->
                                PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber(phoneNumber)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(context as Activity)
                                    .setCallbacks(callback)
                                    .build()
                            }
                        )
                        viewModel.resendCountdown.value = 60
                    },
                    enabled = viewModel.resendCountdown.value == 0
                ),
                color = if (viewModel.resendCountdown.value == 0) MaterialTheme.colorScheme.primary else Color.LightGray,
                text = "Kirim Ulang"
            )
        }
    }
}