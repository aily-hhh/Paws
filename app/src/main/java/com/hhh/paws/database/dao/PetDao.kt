package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Pet
import com.hhh.paws.util.UiState

interface PetDao {
    fun getPet(petName: String, result: (UiState<Pet>) -> Unit)
    fun getNamesPet(result: (UiState<List<String>>) -> Unit)
    fun updatePet(pet: Pet, result: (UiState<Int>) -> Unit)
    fun newPet(pet: Pet, result: (UiState<Int>) -> Unit)
    fun deletePet(petName: String, result: (UiState<Int>) -> Unit)
}