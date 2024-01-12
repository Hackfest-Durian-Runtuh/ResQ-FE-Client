package com.example.resq.model.domain.home

import com.example.resq.model.domain.general.PhoneNumberDomain

data class HomeFavoriteNumberDomain(
    val id:String,
    val name:String,
    val location:String,
    val numbers:List<PhoneNumberDomain>
)
