package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.repository.DehelmintizationRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DehelmintizationViewModel @Inject constructor(private val repository: DehelmintizationRepository): ViewModel() {

    private var _dehelmintizationList = MutableLiveData<UiState<List<Dehelmintization>>>()
    val dehelmintizationList: LiveData<UiState<List<Dehelmintization>>> get() = _dehelmintizationList
    fun getAllDehelmintization(petName: String) {
        _dehelmintizationList.value = UiState.Loading
        repository.getAllDehelmintization(petName) {
            _dehelmintizationList.value = it
        }
    }

    private var _addDehelmintization = MutableLiveData<UiState<String>>()
    val addDehelmintization: LiveData<UiState<String>> get() = _addDehelmintization
    fun setDehelmintization(dehelmintization: Dehelmintization, petName: String) {
        _addDehelmintization.value = UiState.Loading
        viewModelScope.launch {
            repository.setDehelmintization(dehelmintization, petName) {
                _addDehelmintization.value = it
            }
        }
    }

    private var _delete = MutableLiveData<UiState<String>>()
    val delete: LiveData<UiState<String>> get() = _delete
    fun deleteDehelmintization(dehelmintizationID: String, petName: String) {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteDehelmintization(dehelmintizationID, petName) {
                _delete.value = it
            }
        }
    }
 }