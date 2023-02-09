package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.model.Vaccine
import com.hhh.paws.database.repository.VaccinesRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VaccinesViewModel @Inject constructor(private val repository: VaccinesRepository): ViewModel() {

    private var _allVaccines: MutableLiveData<UiState<List<Vaccine>>> = MutableLiveData()
    val allVaccines: LiveData<UiState<List<Vaccine>>> get() = _allVaccines
    fun getAllVaccines(petName: String) {
        _allVaccines.value = UiState.Loading
        viewModelScope.launch {
            repository.getAllVaccines(petName) {
                _allVaccines.value = it
            }
        }
    }

    private var _addVaccine: MutableLiveData<UiState<String>> = MutableLiveData()
    val addVaccine: LiveData<UiState<String>> get() = _addVaccine
    fun setVaccine(petName: String, vaccine: Vaccine) {
        _addVaccine.value = UiState.Loading
        viewModelScope.launch {
            repository.setVaccine(petName, vaccine) {
                _addVaccine.value = it
            }
        }
    }

    private var _removeVaccine: MutableLiveData<UiState<String>> = MutableLiveData()
    val removeVaccine: LiveData<UiState<String>> get() = _removeVaccine
    fun deleteVaccine(petName: String, vaccineID: String) {
        _removeVaccine.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteVaccine(petName, vaccineID) {
                _removeVaccine.value = it
            }
        }
    }
}