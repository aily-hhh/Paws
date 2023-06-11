package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.model.Identification
import com.hhh.paws.database.repository.IdentificationRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IdentificationViewModel @Inject constructor(private val repository: IdentificationRepository): ViewModel() {

    private var _identificationInstance = MutableLiveData<UiState<Identification>>()
    val identificationInstance: LiveData<UiState<Identification>> get() = _identificationInstance
    fun getIdentificationInstance(petName: String) {
        _identificationInstance.value = UiState.Loading
        viewModelScope.launch {
            repository.getIdentification(petName) {
                _identificationInstance.value = it
            }
        }
    }

    private var _identificationAdd = MutableLiveData<UiState<String>>()
    val identificationAdd: LiveData<UiState<String>> get() = _identificationAdd
    fun setIdentificationInstance(petName: String, identification: Identification) {
        _identificationAdd.value = UiState.Loading
        viewModelScope.launch {
            repository.setIdentification(petName, identification) {
                _identificationAdd.value = it
            }
        }
    }
}