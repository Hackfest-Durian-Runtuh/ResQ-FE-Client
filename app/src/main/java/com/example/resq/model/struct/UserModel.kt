package com.example.resq.model.struct

import com.google.firebase.Timestamp

data class UserModel(
    val uid: String? = "",
    val name: String? = "",
    val nik: String? = "",
    val phone_number: String? = "",
    val admin: Boolean? = false,
    val created_at: Timestamp?
)
