package com.example.mobile_hw3.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Upsert
    suspend fun upsertNote(user: User)

    @Delete
    suspend fun deleteNote(user: User)

    @Query("SELECT * FROM User")
    fun getAllNotes(): Flow<List<User>>
}