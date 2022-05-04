package com.example.estrellitas.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusicEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val audio : String,
    val title:String="",
)