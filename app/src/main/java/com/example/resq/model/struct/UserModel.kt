package com.example.resq.model.struct

data class UserModel(
    val uid: String,
    val biodata_id:String,
    val saya: Boolean,
    val fullname: String,
    val nickname: String,
    val phone_number:String,
    val golongan_darah: String,
    val berat_badan: String,
    val tinggi_badan: String,
    val nik: String,
    val tempat_lahir:String,
    val tanggal_lahir:String,
    val asuransi:String,
    val nomor_asuransi:String,
    val penyakit: List<Map<String, String>>
)
