package com.hhh.paws.database.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hhh.paws.R
import com.hhh.paws.database.dao.UserDao
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class UserRepository @Inject constructor(private val database: FirebaseFirestore): UserDao {
    override suspend fun deleteUser(result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        val storageReference = FirebaseStorage.getInstance().reference

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .get().addOnSuccessListener {
                for (document in it) {
                    Log.d(ContentValues.TAG, document.id)
                    database.collection(FireStoreTables.USER).document(uID)
                        .collection(FireStoreTables.PET).document(document.id)
                        .collection(FireStoreTables.GALLERY).get()
                        .addOnSuccessListener { gallery ->
                            for (image in gallery) {
                                val deleteItem: StorageReference = storageReference.child("images/${image.id}")
                                deleteItem.delete().addOnFailureListener {
                                     result.invoke(UiState.Failure("${R.string.error}"))
                                }
                            }
                        }.addOnFailureListener {
                            result.invoke(UiState.Failure("${R.string.error}"))
                        }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }

        database.collection(FireStoreTables.USER).document(uID).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Failure("${R.string.deleted}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}