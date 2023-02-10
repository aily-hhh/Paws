package com.hhh.paws.database.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.R
import com.hhh.paws.database.dao.ReproductionDao
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class ReproductionRepository @Inject constructor(private val database: FirebaseFirestore) : ReproductionDao {
    override fun getAllReproduction(
        petName: String,
        result: (UiState<List<Reproduction>>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.REPRODUCTION).get()
            .addOnSuccessListener {
                val list: MutableList<Reproduction> = mutableListOf()
                for (document in it) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")

                    val reproduction = Reproduction()
                    reproduction.id = document.id
                    reproduction.dateOfBirth = document.data["dateOfBirth"].toString()
                    reproduction.dateOfHeat = document.data["dateOfHeat"].toString()
                    reproduction.dateOfMating = document.data["dateOfMating"].toString()
                    reproduction.numberOfTheLitter = document.data["numberOfTheLitter"].toString()

                    list.add(reproduction)
                }
                result.invoke(UiState.Success(list))
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override suspend fun setReproduction(
        petName: String,
        reproduction: Reproduction,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.REPRODUCTION).document(reproduction.id)
            .set(reproduction).addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.added}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override suspend fun deleteReproduction(
        petName: String,
        reproductionID: String,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.REPRODUCTION).document(reproductionID)
            .delete().addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.deleted}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}