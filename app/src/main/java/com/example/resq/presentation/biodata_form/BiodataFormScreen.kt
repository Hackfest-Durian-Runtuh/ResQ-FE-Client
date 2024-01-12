package com.example.resq.presentation.biodata_form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@ExperimentalMaterial3Api
@Composable
fun BiodataFormScreen(
    modifier: Modifier = Modifier,
    id: String = "",
    isNewData: Boolean = true,
    navController: NavController
) {
    val viewModel = hiltViewModel<BiodataFormViewModel>()
    val focus = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        if (id.isNotEmpty()) {
            //TODO Call API Here
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Detail Biodata")
                },
                navigationIcon = {
                    if (!isNewData) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "")
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Informasi Dasar", style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF151619),

                        )
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.fullname.value,
                    onValueChange = {
                        viewModel.fullname.value = it
                    },
                    placeholder = { Text(text = "Nama Lengkap") },
                    label = {Text(text = "Nama Lengkap")})
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.nickname.value,
                    onValueChange = {
                        viewModel.nickname.value
                    },
                    placeholder = { Text(text = "Nama Panggilan") },
                    label = {Text(text = "Nama Panggilan")})
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.expandBloodType.value = true
                            }, value = viewModel.bloodType.value,
                        placeholder = {
                            Text(text = "Golongan Darah")
                        },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                focus.clearFocus(true)
                                viewModel.expandBloodType.value = !viewModel.expandBloodType.value
                            }) {
                                Icon(
                                    imageVector = if (viewModel.expandBloodType.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = ""
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = viewModel.expandBloodType.value,
                        onDismissRequest = {
                            viewModel.expandBloodType.value = false
                        }
                    ) {
                        listOf(
                            "A+",
                            "A-",
                            "B+",
                            "B-",
                            "O+",
                            "O-",
                            "AB+",
                            "AB-"
                        ).forEach {
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(text = it) },
                                onClick = {
                                    viewModel.bloodType.value = it
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.weight.value,
                    onValueChange = {
                        viewModel.weight.value = it
                    },
                    placeholder = { Text(text = "Berat Badan (kg)") },
                    label = {Text(text = "Nama Panggilan")})
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.height.value,
                    onValueChange = {
                        viewModel.height.value = it
                    },
                    placeholder = { Text(text = "Tinggi Badan (cm)") },
                    label = {Text(text = "Tinggi Badan (cm)")})
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Administrasi & Asuransi", style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF151619),

                        )
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.fullname.value,
                    onValueChange = {
                        viewModel.nik.value = it
                    },
                    placeholder = { Text(text = "NIK") },
                    label = {Text(text = "NIK")})
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.tempatLahir.value,
                    onValueChange = {
                        viewModel.nickname.value
                    },
                    placeholder = { Text(text = "Tempat Lahir") },
                    label = { Text(text = "Tempat Lahir") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.tanggalLahir.value,
                    onValueChange = {
                        viewModel.weight.value = it
                    },
                    placeholder = { Text(text = "Tanggal Lahir (cth: 01/01/2001)") },
                    label = { Text(text = "Tanggal Lahir") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.namaAsuransi.value,
                    onValueChange = {
                        viewModel.height.value = it
                    },
                    placeholder = { Text(text = "Nama Asuransi (cth: BPJS)") },
                    label = { Text(text = "Nama Asuransi") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.noAsuransi.value,
                    onValueChange = {
                        viewModel.height.value = it
                    },
                    placeholder = { Text(text = "Nomor Asuransi") },
                    label = {Text(text = "No. Asuransi")})
            }
        }
    }
}