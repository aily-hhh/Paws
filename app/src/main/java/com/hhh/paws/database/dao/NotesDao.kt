package com.hhh.paws.database.dao

import com.hhh.paws.database.model.Notes
import com.hhh.paws.util.UiState

interface NotesDao {
    fun getAllNotes(petName: String, result: (UiState<List<Notes>>) -> Unit)
    fun getNote(noteID: String, petName: String, result: (UiState<Notes>) -> Unit)
    fun addNote(note: Notes, petName: String, result: (UiState<String>) -> Unit)
    fun updateNote(note: Notes, petName: String, result: (UiState<String>) -> Unit)
    fun deleteNote(noteID: String, petName: String, result: (UiState<String>) -> Unit)
}