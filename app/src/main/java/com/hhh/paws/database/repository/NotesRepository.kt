package com.hhh.paws.database.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hhh.paws.R
import com.hhh.paws.database.dao.NotesDao
import com.hhh.paws.database.model.Notes
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import java.util.*
import javax.inject.Inject


class NotesRepository @Inject constructor(private val database: FirebaseFirestore): NotesDao {

    override fun getAllNotes(petName: String, result: (UiState<List<Notes>>) -> Unit) {
        val notes: MutableList<Notes> = mutableListOf()
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName).collection(FireStoreTables.NOTES)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val newNote = Notes()
                    newNote.id = document.id
                    newNote.title = document.data["title"].toString()
                    newNote.description = document.data["description"].toString()
                    newNote.date = document.data["date"].toString()
                    newNote.pinned = document.data["pinned"] as Boolean?
                    notes.add(newNote)
                }
                result.invoke(UiState.Success(notes))
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }

    override fun getNote(noteID: String, petName: String, result: (UiState<Notes>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName).collection(FireStoreTables.NOTES)
            .document(noteID)
            .get()
            .addOnSuccessListener {
                val note: Notes = it.toObject(Notes::class.java)!!
                result.invoke(UiState.Success(note))
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }

    override suspend fun addNote(note: Notes, petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.NOTES).document(UUID.randomUUID().toString()).set(note)
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note added"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }

    override suspend fun updateNote(note: Notes, petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.NOTES).document(note.id).set(note)
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note updated"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }

    override suspend fun deleteNote(noteID: String, petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.NOTES).document(noteID).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note deleted"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }
}