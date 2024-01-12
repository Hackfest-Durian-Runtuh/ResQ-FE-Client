package com.example.resq.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.resq.model.entity.FavoriteItemEntity

@Dao
abstract class FavoriteItemDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    abstract fun insertNewFavorite(item:FavoriteItemEntity)

    @Delete
    abstract fun deleteFavorite(item:FavoriteItemEntity)

    @Query("select * from favoriteitementity")
    abstract fun getAllFavoriteItem():List<FavoriteItemEntity>
}