package com.example.resq.model.domain.map

import com.example.resq.model.domain.general.PhoneNumberDomain

data class MapPickedNumberDomain(
    val id:String,
    val name:String,
    val location:String,
    val numbers:List<PhoneNumberDomain>
)
