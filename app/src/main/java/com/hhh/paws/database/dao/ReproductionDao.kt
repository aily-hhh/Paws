package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.util.UiState

interface ReproductionDao {
    fun getAllReproduction(petName: String, result: (UiState<List<Reproduction>>) -> Unit)
    suspend fun setReproduction(petName: String, reproduction: Reproduction, result: (UiState<String>) -> Unit)
    suspend fun deleteReproduction(petName: String, reproductionID: String, result: (UiState<String>) -> Unit)
}