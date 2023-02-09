package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.database.repository.ReproductionRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReproductionViewModel @Inject constructor(private val repository: ReproductionRepository): ViewModel() {

    private val _allReproduction: MutableLiveData<UiState<List<Reproduction>>> = MutableLiveData()
    val allReproduction: LiveData<UiState<List<Reproduction>>> = _allReproduction
    fun getAllReproduction(petName: String) {
        _allReproduction.value = UiState.Loading
        repository.getAllReproduction(petName) {
            _allReproduction.value = it
        }
    }

    private val _addReproduction: MutableLiveData<UiState<String>> = MutableLiveData()
    val addReproduction: LiveData<UiState<String>> = _addReproduction
    fun setReproduction(petName: String, reproduction: Reproduction) {
        _addReproduction.value = UiState.Loading
        viewModelScope.launch {
            repository.setReproduction(petName, reproduction) {
                _addReproduction.value = it
            }
        }
    }

    private val _removeReproduction: MutableLiveData<UiState<String>> = MutableLiveData()
    val removeReproduction: LiveData<UiState<String>> = _removeReproduction
    fun deleteReproduction(petName: String, reproductionID: String) {
        _removeReproduction.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteReproduction(petName, reproductionID) {
                _removeReproduction.value = it
            }
        }
    }
}