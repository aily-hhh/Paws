package com.hhh.paws.database.dao

import com.hhh.paws.database.model.SurgicalProcedure
import com.hhh.paws.util.UiState

interface ProcedureDao {
    suspend fun getAllProcedures(petName: String, result: (UiState<List<SurgicalProcedure>>) -> Unit)
    suspend fun setProcedure(petName: String, procedure: SurgicalProcedure, result: (UiState<String>) -> Unit)
    suspend fun deleteProcedure(petName: String, procedure: SurgicalProcedure, result: (UiState<String>) -> Unit)
}