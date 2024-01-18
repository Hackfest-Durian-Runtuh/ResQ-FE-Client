package com.example.resq.presentation.biodata_form

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.model.struct.BiodataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiodataFormViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val fullname = mutableStateOf("")
    val nickname = mutableStateOf("")
    val expandBloodType = mutableStateOf(false)
    val bloodType = mutableStateOf("")
    val weight = mutableStateOf("")
    val height = mutableStateOf("")

    val nik = mutableStateOf("")
    val tempatLahir = mutableStateOf("")
    val tanggalLahir = mutableStateOf("")
    val namaAsuransi = mutableStateOf("")
    val noAsuransi = mutableStateOf("")

    val penyakit = mutableStateListOf(
        mapOf("nama_penyakit" to mutableStateOf(""), "tahun_penyakit" to mutableStateOf(""))
    )

    fun saveBiodata(
        onSuccess: () -> Unit,
        onFailed: (e:Exception) -> Unit
    ) {
        repository.saveUserDataInputNew(
            model = BiodataModel(
                nik = nik.value,
                asuransi = namaAsuransi.value,
                nomor_asuransi = noAsuransi.value,
                fullnama = fullname.value,
                nickname = nickname.value,
                penyakit = penyakit.map {
                    mapOf(
                        "nama_penyakit" to (it["nama_penyakit"]?.value ?: ""),
                        "tahun_penyakit" to (it["tahun_penyakit"]?.value ?: "")
                    )
                },
                tempat_lahir = tempatLahir.value,
                tinggi_badan = height.value,
                tanggal_lahir = tanggalLahir.value,
                berat_badan =weight.value,
                golongan_darah = bloodType.value
            ),
            onSuccess, onFailed
        )
    }
}