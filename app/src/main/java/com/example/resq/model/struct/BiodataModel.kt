package com.example.resq.model.struct

data class BiodataModel(
    val fullnama: String,
    val nickname: String,
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