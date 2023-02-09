package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.model.Treatment
import com.hhh.paws.database.repository.TreatmentRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TreatmentViewModel @Inject constructor(private val repository: TreatmentRepository): ViewModel() {

    private var _allTreatment: MutableLiveData<UiState<List<Treatment>>> = MutableLiveData()
    val allTreatment: LiveData<UiState<List<Treatment>>> get() = _allTreatment
    fun getAllTreatment(petName: String) {
        _allTreatment.value = UiState.Loading
        viewModelScope.launch {
            repository.getAllTreatments(petName) {
                _allTreatment.value = it
            }
        }
    }

    private var _addTreatment: MutableLiveData<UiState<String>> = MutableLiveData()
    val addTreatment: LiveData<UiState<String>> get() = _addTreatment
    fun setTreatment(petName: String, treatment: Treatment) {
        _addTreatment.value = UiState.Loading
        viewModelScope.launch {
            repository.setTreatment(petName, treatment) {
                _addTreatment.value = it
            }
        }
    }

    private var _removeTreatment: MutableLiveData<UiState<String>> = MutableLiveData()
    val removeTreatment: LiveData<UiState<String>> get() = _removeTreatment
    fun deleteTreatment(petName: String, treatmentID: String) {
        _removeTreatment.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteTreatment(petName, treatmentID) {
                _removeTreatment.value = it
            }
        }
    }

}