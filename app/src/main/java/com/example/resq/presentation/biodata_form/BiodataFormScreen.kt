package com.example.resq.presentation.biodata_form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.resq.helper.LoadingHandler
import com.example.resq.helper.SnackbarHandler

@ExperimentalMaterial3Api
@Composable
fun BiodataFormScreen(
    modifier: Modifier = Modifier,
    id: String = "",
    isNewData: Boolean = true,
    isSaya: Boolean = false,
    navController: NavController
) {
    val viewModel = hiltViewModel<BiodataFormViewModel>()
    val focus = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        if (id.isNotEmpty()) {
            viewModel.getBiodataDefault(id)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = if (isSaya) "Registrasi Lanjutan" else "Detail Biodata")
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
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        LoadingHandler.loading()

                        if(id.isEmpty()){
                            if(!isSaya){
                                viewModel.saveBiodataPasien(
                                    onSuccess = {
                                        LoadingHandler.dismiss()
                                        SnackbarHandler.showSnackbar("Data berhasil disimpan")
                                    },
                                    onFailed = {
                                        LoadingHandler.dismiss()
                                        SnackbarHandler.showSnackbar(it.message.toString())
                                    }
                                )
                            }else {
                                viewModel.saveBiodataSaya(
                                    onSuccess = {
                                        LoadingHandler.dismiss()
                                        SnackbarHandler.showSnackbar("Data berhasil disimpan")
                                    },
                                    onFailed = {
                                        LoadingHandler.dismiss()
                                        SnackbarHandler.showSnackbar(it.message.toString())
                                    }
                                )
                            }
                        }else {
                            viewModel.updateBiodataPasien(
                                biodata_id = id,
                                onSuccess = {
                                    LoadingHandler.dismiss()
                                    SnackbarHandler.showSnackbar("Data berhasil disimpan")
                                },
                                onFailed = {
                                    LoadingHandler.dismiss()
                                    SnackbarHandler.showSnackbar(it.message.toString())
                                }
                            )
                        }

                    }) {
                    Text(text = "Simpan")
                }
            }
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
                    label = { Text(text = "Nama Lengkap") })

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.nickname.value,
                    onValueChange = {
                        viewModel.nickname.value = it
                    },
                    placeholder = { Text(text = "Nama Panggilan") },
                    label = { Text(text = "Nama Panggilan") })
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
                                    viewModel.expandBloodType.value = false
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Berat Badan (kg)") },
                    label = { Text(text = "Berat Badang (kg)") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.height.value,
                    onValueChange = {
                        viewModel.height.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Tinggi Badan (cm)") },
                    label = { Text(text = "Tinggi Badan (cm)") })
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
                    value = viewModel.nik.value,
                    onValueChange = {
                        viewModel.nik.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "NIK") },
                    label = { Text(text = "NIK") })

                if (isSaya) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.phoneNumber.value,
                        onValueChange = {
                            viewModel.phoneNumber.value = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text(text = "Nomor Handphone") },
                        label = { Text(text = "Nomor Handphone") }
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.tempatLahir.value,
                    onValueChange = {
                        viewModel.tempatLahir.value = it
                    },
                    placeholder = { Text(text = "Tempat Lahir") },
                    label = { Text(text = "Tempat Lahir") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.tanggalLahir.value,
                    onValueChange = {
                        viewModel.tanggalLahir.value = it
                    },
                    placeholder = { Text(text = "Tanggal Lahir (cth: 01/01/2001)") },
                    label = { Text(text = "Tanggal Lahir") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.namaAsuransi.value,
                    onValueChange = {
                        viewModel.namaAsuransi.value = it
                    },
                    placeholder = { Text(text = "Nama Asuransi (cth: BPJS)") },
                    label = { Text(text = "Nama Asuransi") })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.noAsuransi.value,
                    onValueChange = {
                        viewModel.noAsuransi.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Nomor Asuransi") },
                    label = { Text(text = "No. Asuransi") })
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Riwayat Penyakit", style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF151619),

                        )
                )

                viewModel.penyakit.forEachIndexed { index, map ->
                    if (index > 0) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .height(1.dp)
                                .background(Color.DarkGray)
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = map.get("nama_penyakit")?.value ?: "",
                        onValueChange = {
                            map["nama_penyakit"]?.value = it
                        },
                        placeholder = { Text(text = "Nama Penyakit") },
                        label = { Text(text = "Nama Penyakit") })
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = map.get("tahun_penyakit")?.value ?: "",
                        onValueChange = {
                            map["tahun_penyakit"]?.value = it
                        },
                        placeholder = { Text(text = "Tahun Terkena Penyakit") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(text = "Tahun Terkena Penyakit") })

                    if (index > 0) {
                        Text(
                            modifier = Modifier.clickable { viewModel.penyakit.removeAt(index) },
                            text = "Hapus",
                            color = Color.Red
                        )
                    }
                }

                Button(onClick = {
                    viewModel.penyakit.add(
                        mapOf(
                            "nama_penyakit" to mutableStateOf(""),
                            "tahun_penyakit" to mutableStateOf("")
                        )
                    )
                }) {
                    Text(text = "Tambah Riwayat Penyakit")
                }
            }
        }
    }
}