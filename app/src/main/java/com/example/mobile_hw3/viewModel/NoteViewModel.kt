package com.example.mobile_hw3.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobile_hw3.roomDb.User
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository): ViewModel() {
    fun getUsers() = repository.getAllUsers().asLiveData(viewModelScope.coroutineContext)

    fun upsertUser(user: User){
        viewModelScope.launch {
            repository.upsertUser(user)
        }
    }
}