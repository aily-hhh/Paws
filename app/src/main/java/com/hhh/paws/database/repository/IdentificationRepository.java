package com.hhh.paws.database.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hhh.paws.database.dao.IdentificationDao;
import com.hhh.paws.database.model.Identification;
import com.hhh.paws.util.FireStoreTables;
import com.hhh.paws.util.UiState;

import java.util.Objects;

import javax.inject.Inject;


public class IdentificationRepository implements IdentificationDao {

    private final FirebaseFirestore database;
    @Inject
    public IdentificationRepository(FirebaseFirestore database) {
        this.database = database;
    }

    @Override
    public void getIdentification(String petName) {
        String uID = FirebaseAuth.getInstance().getUid();

        database.collection(FireStoreTables.USER).document(uID)
                .collection(FireStoreTables.PET).document(petName)
                .collection(FireStoreTables.IDENTIFICATION).document(FireStoreTables.IDENTIFICATION)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Identification identification = new Identification();
                        identification.setDateOfTattooing(
                                Objects.requireNonNull(documentSnapshot
                                                .get("dateOfTattooing"))
                                                .toString()
                        );
                        identification.setDateOfMicrochipping(
                                Objects.requireNonNull(documentSnapshot
                                                .get("dateOfMicrochipping"))
                                                .toString()
                        );
                        identification.setMicrochipLocation(
                                Objects.requireNonNull(documentSnapshot
                                                .get("microchipLocation"))
                                                .toString()
                        );
                        identification.setMicrochipNumber(
                                Objects.requireNonNull(documentSnapshot
                                                .get("microchipNumber"))
                                                .toString()
                        );
                        identification.setTattooNumber(
                                Objects.requireNonNull(documentSnapshot
                                                .get("tattooNumber"))
                                                .toString()
                        );


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public UiState<String> setIdentification(String petName, Identification identification) {
        return null;
    }
}
