package com.example.resq.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.resq.data.room.dao.FavoriteItemDao
import com.example.resq.model.entity.FavoriteItemEntity

@Database(
    entities = [
        FavoriteItemEntity::class
    ],
    version = 1
)
@TypeConverters(RoomConverters::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun favoriteItemDao():FavoriteItemDao
}