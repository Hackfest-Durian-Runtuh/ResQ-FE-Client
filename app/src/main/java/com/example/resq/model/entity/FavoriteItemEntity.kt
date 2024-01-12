package com.example.resq.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.resq.model.domain.general.PhoneNumberDomain

@Entity
data class FavoriteItemEntity(
    @PrimaryKey val em_pvd_id:String,
    val em_pvd_name:String?,
    val location:String?,
    val numbers:FavoriteItemPhoneNumbers
)

data class FavoriteItemPhoneNumbers(
    val data:List<PhoneNumberDomain>
)
