package com.hhh.paws.database.viewModel

import androidx.lifecycle.ViewModel
import com.hhh.paws.database.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {
    val user = repository.user
}