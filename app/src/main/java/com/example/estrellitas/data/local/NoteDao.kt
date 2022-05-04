package com.example.estrellitas.data.local

import androidx.room.*
import com.example.estrellitas.data.models.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM noteentity")
    suspend fun getNotes():List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(noteEntity: NoteEntity):Long

    @Update
    suspend fun updateNote(noteEntity: NoteEntity):Int

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity):Int
}