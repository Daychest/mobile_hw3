package com.example.mobile_hw3.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val noteName: String,
    val noteBody: String,
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0
)
