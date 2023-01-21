package com.hhh.paws.database.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.hhh.paws.R
import com.hhh.paws.database.dao.PetDao
import com.hhh.paws.database.model.Pet
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject


var uID = FirebaseAuth.getInstance().currentUser!!.uid

class PetRepository @Inject constructor(private val database: FirebaseFirestore): PetDao {

    override fun getPet(petName: String, result: (UiState<Pet>) -> Unit) {
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .get().addOnSuccessListener {
                val pet: Pet = it.toObject(Pet::class.java)!!
                result.invoke(UiState.Success(pet))
            }
            .addOnFailureListener{
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getNamesPet(result: (UiState<List<String>>) -> Unit) {
        val pets: MutableList<String> = mutableListOf()

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
                result.invoke(UiState.Failure(exception.localizedMessage))
            }
    }

    override fun updatePet(pet: Pet, result: (UiState<String>) -> Unit) {
        val updatePet = database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(pet.name).set(pet)
            .addOnSuccessListener {
                result.invoke(UiState.Success(R.string.updated.toString()))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun newPet(pet: Pet, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(pet.name).set(pet)
            .addOnSuccessListener {
                result.invoke(UiState.Success(R.string.created.toString()))
            }
            .addOnFailureListener {
                UiState.Failure(it.localizedMessage)
            }
    }

    override fun deletePet(petName: String, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success(R.string.deleted.toString()))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }
}