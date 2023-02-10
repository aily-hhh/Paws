package com.hhh.paws.database.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.R
import com.hhh.paws.database.dao.TreatmentDao
import com.hhh.paws.database.model.Treatment
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class TreatmentRepository @Inject constructor(private val database: FirebaseFirestore): TreatmentDao {

    override fun getAllTreatments(petName: String, result: (UiState<List<Treatment>>) -> Unit) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.TREATMENT).get()
            .addOnSuccessListener {
                val list: MutableList<Treatment> = mutableListOf()
                for (document in it) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")

                    val treatment = Treatment()
                    treatment.id = document.id
                    treatment.date = document.data["date"].toString()
                    treatment.name = document.data["name"].toString()
                    treatment.manufacturer = document.data["manufacturer"].toString()
                    treatment.veterinarian = document.data["veterinarian"].toString()

                    list.add(treatment)
                }
                result.invoke(UiState.Success(list))

            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override suspend fun setTreatment(
        petName: String,
        treatment: Treatment,
        result: (UiState<String>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.TREATMENT).document(treatment.id.toString()).set(treatment)
            .addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.added}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override suspend fun deleteTreatment(
        petName: String,
        treatmentID: String,
        result: (UiState<String>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.TREATMENT).document(treatmentID).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.deleted}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}