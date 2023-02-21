package com.hhh.paws.database.dao

import com.hhh.paws.util.UiState

interface UserDao {
    suspend fun deleteUser(result: (UiState<String>) -> Unit)
}