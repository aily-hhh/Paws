package com.hhh.paws.database.viewModel

import androidx.lifecycle.ViewModel
import com.hhh.paws.database.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor( private val repository: NotesRepository): ViewModel() {


}