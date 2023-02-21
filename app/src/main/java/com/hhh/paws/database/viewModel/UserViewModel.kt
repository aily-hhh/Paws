package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.paws.database.repository.UserRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    private val _delete: MutableLiveData<UiState<String>> = MutableLiveData()
    var delete: LiveData<UiState<String>> = _delete
    fun deleteUser() {
        _delete.value = UiState.Loading
        viewModelScope.launch {
            repository.deleteUser {
                _delete.value = it
            }
        }
    }
}