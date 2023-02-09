package com.hhh.paws.database.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hhh.paws.database.dao.VaccinesDao
import com.hhh.paws.database.model.Vaccine
import com.hhh.paws.util.FireStoreTables
import com.hhh.paws.util.UiState
import javax.inject.Inject

class VaccinesRepository @Inject constructor(private val database: FirebaseFirestore): VaccinesDao {

    override fun getAllVaccines(petName: String, result: (UiState<List<Vaccine>>) -> Unit) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.VACCINES).get()
            .addOnSuccessListener {
                val list: MutableList<Vaccine> = mutableListOf()
                for (document in it) {
                    val vaccine = Vaccine()
                    vaccine.id = document.id
                    vaccine.dateOfVaccination = document.data["dateOfVaccination"].toString()
                    vaccine.name = document.data["name"].toString()
                    vaccine.type = document.data["type"].toString()
                    vaccine.manufacturer = document.data["manufacturer"].toString()
                    vaccine.validUntil = document.data["validUntil"].toString()
                    vaccine.veterinarian = document.data["veterinarian"].toString()

                    list.add(vaccine)
                }
                result.invoke(UiState.Success(list))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("error"))
            }
    }

    override suspend fun setVaccine(
        petName: String,
        vaccine: Vaccine,
        result: (UiState<String>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.VACCINES).document(vaccine.id.toString()).set(vaccine)
            .addOnSuccessListener {
                result.invoke(UiState.Success("added"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("error"))
            }
    }

    override suspend fun deleteVaccine(
        petName: String,
        vaccineID: String,
        result: (UiState<String>) -> Unit
    ) {
        val uID: String = FirebaseAuth.getInstance().currentUser!!.uid

        database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
            .document(petName).collection(FireStoreTables.VACCINES).document(vaccineID).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("deleted"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("error"))
            }
    }
}