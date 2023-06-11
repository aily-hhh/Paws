package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Identification
import com.hhh.paws.util.UiState

interface IdentificationDao {
    suspend fun getIdentification(petName: String, result: (UiState<Identification>) -> Unit)
    suspend fun setIdentification(petName: String, identification: Identification, result: (UiState<String>) -> Unit)
}