package com.hhh.paws.database.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.R
import com.hhh.paws.database.dao.DehelmintizationDao
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class DehelmintizationRepository @Inject constructor(private val database: FirebaseFirestore) : DehelmintizationDao {

    override fun getAllDehelmintization(
        petName: String,
        result: (UiState<List<Dehelmintization>>) -> Unit
    ) {
        val dehelmintizationList: MutableList<Dehelmintization> = mutableListOf()
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.DEHELMINTIZATION)
            .get().addOnSuccessListener {
                for (document in it) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val dehelmintization = Dehelmintization()
                    dehelmintization.id = document.id
                    dehelmintization.date = document.data["date"].toString()
                    dehelmintization.description = document.data["description"].toString()
                    dehelmintization.dose = document.data["dose"].toString()
                    dehelmintization.manufacturer = document.data["manufacturer"].toString()
                    dehelmintization.name = document.data["name"].toString()
                    dehelmintization.time = document.data["time"].toString()
                    dehelmintization.veterinarian = document.data["veterinarian"].toString()
                    dehelmintizationList.add(dehelmintization)
                }
                result.invoke(UiState.Success(dehelmintizationList))
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                result.invoke(UiState.Failure(R.string.error.toString()))
            }
    }

    override suspend fun setDehelmintization(
        dehelmintization: Dehelmintization,
        petName: String,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.DEHELMINTIZATION)
            .document(dehelmintization.id.toString()).set(dehelmintization)
            .addOnSuccessListener {
                result.invoke(UiState.Success("added"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("error"))
            }
    }

    override suspend fun deleteDehelmintization(
        dehelmintizationID: String,
        petName: String,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.DEHELMINTIZATION)
            .document(dehelmintizationID).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("deleted"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("error"))
            }
    }
}