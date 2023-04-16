package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hhh.paws.database.model.Pet
import com.hhh.paws.database.repository.PetRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PetViewModel @Inject constructor(private val repository: PetRepository): ViewModel() {

    private val _pet = MutableLiveData<UiState<Pet>>()
    val pet: LiveData<UiState<Pet>> get() = _pet
    fun getPet(petName: String) {
        _pet.value = UiState.Loading
        repository.getPet(petName) {
            _pet.value = it
        }
    }

    private val _namesPet = MutableLiveData<UiState<List<String>>>()
    val namesPet: LiveData<UiState<List<String>>> get() = _namesPet
    fun getNamesPet() {
        _namesPet.value = UiState.Loading
        repository.getNamesPet {
            _namesPet.value = it
        }
    }

    private val _update = MutableLiveData<UiState<Int>>()
    val update: LiveData<UiState<Int>> get() = _update
    fun updatePet(pet: Pet) {
        _update.value = UiState.Loading
        repository.updatePet(pet) {
            _update.value = it
        }
    }

    private val _addNewPet = MutableLiveData<UiState<Int>>()
    val addNewPet: LiveData<UiState<Int>> get() = _addNewPet
    fun newPet(pet: Pet) {
        _addNewPet.value = UiState.Loading
        repository.newPet(pet) {
            _addNewPet.value = it
        }
    }

    private val _delete = MutableLiveData<UiState<Int>>()
    val delete: LiveData<UiState<Int>> get() = _delete
    fun deletePet(petName: String) {
        _delete.value = UiState.Loading
        repository.deletePet(petName) {
            _delete.value = it
        }
    }
}