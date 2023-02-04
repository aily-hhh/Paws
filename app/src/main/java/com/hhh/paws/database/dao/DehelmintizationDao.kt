package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.util.UiState

interface DehelmintizationDao {
    fun getAllDehelmintization(petName: String, result: (UiState<List<Dehelmintization>>) -> Unit)
    suspend fun setDehelmintization(dehelmintization: Dehelmintization, petName: String, result: (UiState<String>) -> Unit)
    suspend fun deleteDehelmintization(dehelmintizationID: String, petName: String, result: (UiState<String>) -> Unit)
}