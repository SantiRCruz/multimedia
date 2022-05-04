package com.example.estrellitas.data.local

import androidx.room.*
import com.example.estrellitas.data.models.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM imageentity")
    suspend fun getImages():List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertImage(imageEntity: ImageEntity):Long

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity):Int
}
