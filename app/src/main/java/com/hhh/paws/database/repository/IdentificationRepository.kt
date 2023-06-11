package com.hhh.paws.database.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.R
import com.hhh.paws.database.dao.IdentificationDao
import com.hhh.paws.database.model.Identification
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class IdentificationRepository @Inject constructor(private val database: FirebaseFirestore): IdentificationDao {

    override suspend fun getIdentification(
        petName: String,
        result: (UiState<Identification>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.IDENTIFICATION).document(petName)
            .get().addOnSuccessListener { documentSnapshot ->
                val identification = Identification()
                if (documentSnapshot["dateOfTattooing"] != null) {
                    identification.dateOfTattooing = documentSnapshot.data
                        ?.get("dateOfTattooing").toString()
                }
                if (documentSnapshot["dateOfMicrochipping"] != null) {
                    identification.dateOfMicrochipping = documentSnapshot.data
                        ?.getOrDefault("dateOfMicrochipping", "").toString()
                }
                if (documentSnapshot["microchipLocation"] != null) {
                    identification.microchipLocation = documentSnapshot.data
                        ?.getOrDefault("microchipLocation", "").toString()
                }
                if (documentSnapshot["microchipNumber"] != null) {
                    identification.microchipNumber = documentSnapshot.data
                        ?.getOrDefault("microchipNumber", "").toString()
                }
                if (documentSnapshot["tattooNumber"] != null) {
                    identification.tattooNumber = documentSnapshot.data
                        ?.getOrDefault("tattooNumber", "").toString()
                }
                result.invoke(UiState.Success(identification))
            }.addOnFailureListener { e ->
                e.localizedMessage?.let {
                    Log.d(
                        "Identification",
                        it
                    )
                }
                result.invoke(UiState.Failure(e.localizedMessage))
            }
    }

    override suspend fun setIdentification(
        petName: String,
        identification: Identification,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().uid

        database.collection(FireStoreTables.USER).document(uID!!)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.IDENTIFICATION).document(petName)
            .set(identification).addOnSuccessListener {
                Log.d("Firestore", "Identification success")
                result.invoke(UiState.Success("${R.string.added}"))
            }.addOnFailureListener {
                Log.d("Firestore", "Identification failed")
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}