package com.example.resq.presentation.user_data_input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.resq.R
import com.example.resq.helper.LoadingHandler
import com.example.resq.helper.SnackbarHandler
import com.example.resq.navhost.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataInputScreen(
    phoneNumber: String,
    navController: NavController
) {
    val viewModel = hiltViewModel<UserDataInputViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 128.dp),
            model = R.drawable.ic_splash,
            contentDescription = ""
        )

        Column(
            modifier = Modifier.padding(vertical = 64.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column {
                Text(text = "Daftar", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Mohon masukkan informasi akun Anda untuk melanjutkan pendaftaran.")
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNumber,
                onValueChange = {},
                enabled = false,
                readOnly = true,
                label = {
                    Text(text = "Nomor Telepon", color = Color.LightGray)
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.nama.value,
                onValueChange = {
                    viewModel.nama.value = it
                },
                label = {
                    Text(text = "Nama")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.nik.value,
                onValueChange = {
                    viewModel.nik.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = "NIK (Nomor Induk Kependudukan)")
                }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text(text = "Dengan masuk atau mendaftar, saya setuju dengan Persyaratan Layanan dan Kebijakan Privasi.")
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    LoadingHandler.loading()
                    viewModel.saveUserDataInput(
                        phoneNumber = phoneNumber,
                        name = viewModel.nama.value,
                        nik = viewModel.nik.value,
                        onSuccess = {
                            LoadingHandler.dismiss()
                            navController.navigate(NavRoutes.BERANDA.name){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                            SnackbarHandler.showSnackbar("Berhasil registrasi, selamat menikmati semua fitur di OneConnect")
                        },
                        onFailed = {
                            LoadingHandler.dismiss()
                            SnackbarHandler.showSnackbar("ERROR: $it")
                        }
                    )
                }
            ) {
                Text(text = "Mendaftar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}