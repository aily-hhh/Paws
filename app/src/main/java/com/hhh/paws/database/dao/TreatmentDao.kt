package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Treatment
import com.hhh.paws.util.UiState

interface TreatmentDao {
    fun getAllTreatments(petName: String, result: (UiState<List<Treatment>>) -> Unit)
    suspend fun setTreatment(petName: String, treatment: Treatment, result: (UiState<String>) -> Unit)
    suspend fun deleteTreatment(petName: String, treatmentID: String, result: (UiState<String>) -> Unit)
}