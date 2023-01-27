package com.hhh.paws.database.repository

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.hhh.paws.R
import com.hhh.paws.database.dao.NotesDao
import com.hhh.paws.database.model.Notes
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
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
                    val newNote = document.toObject(Notes::class.java)
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

    override fun addNote(note: Notes, petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun updateNote(note: Notes, petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun deleteNote(noteID: String, petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
    }
}