package com.hhh.paws.database.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hhh.paws.R
import com.hhh.paws.database.dao.GalleryDao
import com.hhh.paws.database.model.Gallery
import com.hhh.paws.database.model.GalleryImage
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class GalleryRepository @Inject constructor(private val database: FirebaseFirestore): GalleryDao {
    override fun getAllImages(petName: String, result: (UiState<List<String>>) -> Unit) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.GALLERY).orderBy("date").get()
            .addOnSuccessListener {
                val list = mutableListOf<String>()
                for (document in it) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val uriStr = document.id
                    list.add(uriStr)
                }
                result.invoke(UiState.Success(list))
            }.addOnFailureListener {
                result.invoke((UiState.Failure("${R.string.error}")))
            }
    }

    override fun getOneImage(petName: String, image: String, result: (UiState<String>) -> Unit) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.GALLERY).document(image).get()
            .addOnSuccessListener {
                result.invoke(UiState.Success(it.id))
            }.addOnFailureListener {
                result.invoke((UiState.Failure("${R.string.error}")))
            }
    }

    override suspend fun setImage(
        petName: String,
        image: GalleryImage,
        result: (UiState<String>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid
        val storageReference = FirebaseStorage.getInstance().reference

        val ref: StorageReference = storageReference.child("images/${image.id}")
        ref.putFile(image.uri).addOnSuccessListener {
            database.collection(FireStoreTables.USER).document(uID)
                .collection(FireStoreTables.PET).document(petName)
                .collection(FireStoreTables.GALLERY).document(image.id)
                .set(image).addOnSuccessListener {
                    result.invoke(UiState.Success("${R.string.deleted}"))
                }.addOnFailureListener {
                    result.invoke((UiState.Failure("${R.string.error}")))
                }
        }.addOnFailureListener {
            result.invoke((UiState.Failure("${R.string.error}")))
        }
    }

    override suspend fun deleteImage(
        petName: String,
        imageID: String,
        result: (UiState<String>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid
        val storageReference = FirebaseStorage.getInstance().reference

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.GALLERY).document(imageID).delete()
            .addOnSuccessListener {
                val deleteItem: StorageReference = storageReference.child("images/$imageID")
                deleteItem.delete().addOnSuccessListener {
                    result.invoke(UiState.Success("${R.string.deleted}"))
                }.addOnFailureListener {
                    result.invoke(UiState.Failure("${R.string.error}"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}