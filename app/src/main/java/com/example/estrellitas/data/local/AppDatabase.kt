package com.example.estrellitas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.estrellitas.data.models.ImageEntity
import com.example.estrellitas.data.models.MusicEntity
import com.example.estrellitas.data.models.NoteEntity

@Database(entities = [ImageEntity::class,NoteEntity::class,MusicEntity::class],version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun ImageDao():ImageDao
    abstract fun NoteDao():NoteDao
    abstract fun MusicDao():MusicDao

    companion object{
        private var INSTANCE : AppDatabase ?= null

        fun getImageDatabase(context: Context):AppDatabase{
            INSTANCE = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "image_table"
            ).build()
            return INSTANCE!!
        }

        fun getNoteDatabase(context: Context):AppDatabase{
            INSTANCE = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "note_table"
            ).build()
            return INSTANCE!!
        }

        fun getMusicDatabase(context: Context):AppDatabase{
            INSTANCE = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "music_table"
            ).build()
            return INSTANCE!!
        }

    }
}