package com.example.estrellitas.data.local

import androidx.room.*
import com.example.estrellitas.data.models.MusicEntity

@Dao
interface MusicDao {
    @Query("SELECT * FROM musicentity")
    suspend fun getMusic():List<MusicEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMusic(imageEntity: MusicEntity):Long

    @Delete
    suspend fun deleteMusic(imageEntity: MusicEntity):Int
}