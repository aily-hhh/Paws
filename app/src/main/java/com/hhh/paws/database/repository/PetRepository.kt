package com.hhh.paws.database.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hhh.paws.R
import com.hhh.paws.database.dao.PetDao
import com.hhh.paws.database.model.Pet
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import java.util.*
import javax.inject.Inject


class PetRepository @Inject constructor(private val database: FirebaseFirestore): PetDao {

    override fun getPet(petName: String, result: (UiState<Pet>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .get().addOnSuccessListener {
                val pet = Pet()
                pet.name = petName
                pet.species = it.get("species").toString()
                pet.breed = it.get("breed").toString()
                pet.sex = it.get("sex").toString()
                pet.birthday = it.get("birthday").toString()
                pet.hair = it.get("hair").toString()
                pet.photoUri = it.get("photoUri").toString()
                result.invoke((UiState.Success(pet)))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }

    override fun getNamesPet(result: (UiState<List<String>>) -> Unit) {
        val pets: MutableList<String> = mutableListOf()
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    pets.add(document.id)
                }
                result.invoke(UiState.Success(pets))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override fun updatePet(pet: Pet, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        val photoStr = UUID.randomUUID().toString()
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val ref: StorageReference = storageReference.child("images/$photoStr")
        val oldImage: DocumentReference = database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(pet.name)
        oldImage.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                if (snapshot.get("photoUri") != null) {
                    val imageDelete = snapshot.get("photoUri").toString()
                    storageReference.child("images/$imageDelete")
                        .delete().addOnSuccessListener {
                            Log.d(TAG, "onSuccess: deleted file")
                        }.addOnFailureListener {
                            Log.d(TAG, "onFailure: did not delete file")
                        }
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
        oldImage.update("photoUri", photoStr)

        ref.putFile(Uri.parse(pet.photoUri))
            .addOnSuccessListener {
                Log.d(TAG, "onSuccess: image uploaded")
            }.addOnFailureListener {
                Log.d(TAG, "onFailure: did not upload image")
            }
        pet.photoUri = photoStr

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(pet.name).set(pet)
            .addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.updated}"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override fun newPet(pet: Pet, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(pet.name).set(pet)
            .addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.created}"))
            }
            .addOnFailureListener {
                UiState.Failure("${R.string.error}")
            }
    }

    override fun deletePet(petName: String, result: (UiState<String>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.deleted}"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}