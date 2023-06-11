package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.model.SurgicalProcedure
import com.hhh.paws.database.repository.ProcedureRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProcedureViewModel @Inject constructor(private val repository: ProcedureRepository): ViewModel() {

    private var _allProcedures: MutableLiveData<UiState<List<SurgicalProcedure>>> = MutableLiveData()
    val allProcedures: LiveData<UiState<List<SurgicalProcedure>>> get() = _allProcedures
    fun getAllProcedures(petName: String) {
        _allProcedures.value = UiState.Loading
        viewModelScope.launch {
            repository.getAllProcedures(petName) {
                _allProcedures.value = it
            }
        }
    }

    private var _addProcedure: MutableLiveData<UiState<String>> = MutableLiveData()
    val addProcedure: LiveData<UiState<String>> get() = _addProcedure
    fun setProcedure(petName: String, procedure: SurgicalProcedure) {
        _addProcedure.value = UiState.Loading
        viewModelScope.launch {
            repository.setProcedure(petName, procedure) {
                _addProcedure.value = it
            }
        }
    }

    private var _removeProcedure: MutableLiveData<UiState<String>> = MutableLiveData()
    val removeProcedure: LiveData<UiState<String>> get() = _removeProcedure
    fun deleteProcedure(petName: String, procedure: SurgicalProcedure) {
        _removeProcedure.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteProcedure(petName, procedure) {
                _removeProcedure.value = it
            }
        }
    }
}