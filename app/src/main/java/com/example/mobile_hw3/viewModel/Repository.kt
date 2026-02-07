package com.example.mobile_hw3.viewModel

import com.example.mobile_hw3.roomDb.User
import com.example.mobile_hw3.roomDb.UserDatabase

class Repository(private val db: UserDatabase) {
    suspend fun upsertUser(user: User) {
        db.dao.upsertNote(user)
    }

    fun getAllUsers() = db.dao.getAllNotes()
}