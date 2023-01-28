package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.repository.NotesRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.checkerframework.checker.guieffect.qual.UI
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor( private val repository: NotesRepository): ViewModel() {

    private val _allNotes = MutableLiveData<UiState<List<Notes>>>()
    val allNotes: LiveData<UiState<List<Notes>>> get() = _allNotes
    fun getAllNotes(petName: String) {
        _allNotes.value = UiState.Loading
        repository.getAllNotes(petName) {
            _allNotes.value = it
        }
    }

    private val _note = MutableLiveData<UiState<Notes>>()
    val note: LiveData<UiState<Notes>> get() = _note
    fun getNote(noteID: String, petName: String) {
        _note.value = UiState.Loading
        repository.getNote(noteID, petName) {
            _note.value = it
        }
    }

    private val _add = MutableLiveData<UiState<String>>()
    val add: LiveData<UiState<String>> get() = _add
    fun addNote(note: Notes, petName: String) {
        _add.value = UiState.Loading
        repository.addNote(note, petName) {
            _add.value = it
        }
    }

    private val _update = MutableLiveData<UiState<String>>()
    val update: LiveData<UiState<String>> get() = _update
    fun updateNote(note: Notes, petName: String) {
        _update.value = UiState.Loading
        repository.updateNote(note, petName) {
            _update.value = it
        }
    }

    private val _delete = MutableLiveData<UiState<String>>()
    val delete: LiveData<UiState<String>> get() = _delete
    fun deleteNote(noteID: String, petName: String) {
        _delete.value = UiState.Loading
        repository.deleteNote(noteID, petName) {
            _delete.value = it
        }
    }
}