package com.example.resq.data.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.resq.model.entity.FavoriteItemPhoneNumbers
import com.google.gson.Gson

@ProvidedTypeConverter
class RoomConverters {
    @TypeConverter
    fun numbersListToJson(numbers: FavoriteItemPhoneNumbers) = Gson().toJson(numbers)

    @TypeConverter
    fun numbersJsonToList(numbers: String) =
        Gson().fromJson(numbers, FavoriteItemPhoneNumbers::class.java)
}