package com.hhh.paws.database.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.hhh.paws.R
import com.hhh.paws.database.dao.ProcedureDao
import com.hhh.paws.database.model.SurgicalProcedure
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class ProcedureRepository @Inject constructor(private val database: FirebaseFirestore): ProcedureDao {

    override suspend fun getAllProcedures(
        petName: String,
        result: (UiState<List<SurgicalProcedure>>) -> Unit) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.PROCEDURES).get()
            .addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
            .addOnSuccessListener { documents: QuerySnapshot ->
                val list: MutableList<SurgicalProcedure> = ArrayList()
                for (document in documents) {
                    val procedure = SurgicalProcedure()
                    procedure.id = document.id
                    procedure.name = document.data["name"].toString()
                    procedure.type = document.data["type"].toString()
                    procedure.veterinarian = document.data["veterinarian"].toString()
                    procedure.description = document.data["description"].toString()
                    procedure.date = document.data["date"].toString()
                    procedure.anesthesia = document.data["anesthesia"].toString()
                    list.add(procedure)
                }
                result.invoke(UiState.Success(list))
            }
    }

    override suspend fun setProcedure(
        petName: String,
        procedure: SurgicalProcedure,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.PROCEDURES).document(procedure.id!!)
            .set(procedure)
            .addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.added}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }

    override suspend fun deleteProcedure(
        petName: String,
        procedure: SurgicalProcedure,
        result: (UiState<String>) -> Unit
    ) {
        val uID = FirebaseAuth.getInstance().currentUser!!.uid
        database.collection(FireStoreTables.USER).document(uID)
            .collection(FireStoreTables.PET).document(petName)
            .collection(FireStoreTables.PROCEDURES).document(procedure.id!!)
            .delete().addOnSuccessListener {
                result.invoke(UiState.Success("${R.string.deleted}"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("${R.string.error}"))
            }
    }
}