package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Vaccine
import com.hhh.paws.util.UiState

interface VaccinesDao {
    suspend fun getAllVaccines(petName: String, result: (UiState<List<Vaccine>>) -> Unit)
    suspend fun setVaccine(petName: String, vaccine: Vaccine, result: (UiState<String>) -> Unit)
    suspend fun deleteVaccine(petName: String, vaccineID: String, result: (UiState<String>) -> Unit)
}