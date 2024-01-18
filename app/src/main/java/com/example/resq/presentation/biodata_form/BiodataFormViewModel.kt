package com.example.resq.presentation.biodata_form

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.resq.data.Repository
import com.example.resq.model.struct.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
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
    val phoneNumber = mutableStateOf("")

    val penyakit = mutableStateListOf(
        mapOf("nama_penyakit" to mutableStateOf(""), "tahun_penyakit" to mutableStateOf(""))
    )

    fun saveBiodataSaya(
        onSuccess: () -> Unit,
        onFailed: (e: Exception) -> Unit
    ) {
        repository.saveBiodata(
            model = UserModel(
                uid = repository.uid(),
                biodata_id = repository.uid(),
                phone_number = phoneNumber.value,
                saya = true,
                nik = nik.value,
                asuransi = namaAsuransi.value,
                nomor_asuransi = noAsuransi.value,
                fullname = fullname.value,
                nickname = nickname.value,
                penyakit = penyakit.map {
                    mapOf(
                        "nama_penyakit" to (it["nama_penyakit"]?.value ?: ""),
                        "tahun_penyakit" to (it["tahun_penyakit"]?.value ?: "")
                    )
                }.filter {
                    !it["nama_penyakit"].isNullOrEmpty() && !it["tahun_penyakit"].isNullOrEmpty()
                },
                tempat_lahir = tempatLahir.value,
                tinggi_badan = height.value,
                tanggal_lahir = tanggalLahir.value,
                berat_badan = weight.value,
                golongan_darah = bloodType.value
            ),
            biodata_id = repository.uid(),
            onSuccess = onSuccess,
            onFailed = onFailed
        )
    }

    fun saveBiodataPasien(
        onSuccess: () -> Unit,
        onFailed: (e: Exception) -> Unit
    ) {
        val biodata_id = UUID.randomUUID().toString()
        repository.saveBiodata(
            model = UserModel(
                uid = repository.uid(),
                biodata_id = biodata_id,
                phone_number = phoneNumber.value,
                saya = false,
                nik = nik.value,
                asuransi = namaAsuransi.value,
                nomor_asuransi = noAsuransi.value,
                fullname = fullname.value,
                nickname = nickname.value,
                penyakit = penyakit.map {
                    mapOf(
                        "nama_penyakit" to (it["nama_penyakit"]?.value ?: ""),
                        "tahun_penyakit" to (it["tahun_penyakit"]?.value ?: "")
                    )
                }.filter {
                    !it["nama_penyakit"].isNullOrEmpty() && !it["tahun_penyakit"].isNullOrEmpty()
                },
                tempat_lahir = tempatLahir.value,
                tinggi_badan = height.value,
                tanggal_lahir = tanggalLahir.value,
                berat_badan = weight.value,
                golongan_darah = bloodType.value
            ),
            biodata_id = biodata_id,
            onSuccess = onSuccess,
            onFailed = onFailed
        )
    }
}