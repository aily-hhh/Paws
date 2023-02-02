package com.hhh.paws.database.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hhh.paws.R;
import com.hhh.paws.database.dao.IdentificationDao;
import com.hhh.paws.database.model.Identification;
import com.hhh.paws.util.FireStoreTables;
import com.hhh.paws.util.UiState;

import java.util.Objects;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;


public class IdentificationRepository implements IdentificationDao {

    private final FirebaseFirestore database;
    @Inject
    public IdentificationRepository(FirebaseFirestore database) {
        this.database = database;
    }

    @Override
    public void getIdentification(String petName, final Function1 result) {
        String uID = FirebaseAuth.getInstance().getUid();

        database.collection(FireStoreTables.USER).document(uID)
                .collection(FireStoreTables.PET).document(petName)
                .collection(FireStoreTables.IDENTIFICATION).document(FireStoreTables.IDENTIFICATION)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Identification identification = new Identification();
                        if (documentSnapshot.get("dateOfTattooing") != null) {
                            identification.setDateOfTattooing(
                                    String.valueOf(documentSnapshot.getData()
                                            .get("dateOfTattooing"))
                            );
                        }
                        if (documentSnapshot.get("dateOfMicrochipping") != null) {
                            identification.setDateOfMicrochipping(
                                    String.valueOf(documentSnapshot.getData()
                                            .getOrDefault("dateOfMicrochipping", ""))
                            );
                        }
                        if (documentSnapshot.get("microchipLocation") != null) {
                            identification.setMicrochipLocation(
                                    String.valueOf(documentSnapshot.getData()
                                            .getOrDefault("microchipLocation", ""))
                            );
                        }
                        if (documentSnapshot.get("microchipNumber") != null) {
                            identification.setMicrochipNumber(
                                    String.valueOf(documentSnapshot.getData()
                                            .getOrDefault("microchipNumber", ""))
                            );
                        }
                        if (documentSnapshot.get("tattooNumber") != null) {
                            identification.setTattooNumber(
                                    String.valueOf(documentSnapshot.getData()
                                            .getOrDefault("tattooNumber", ""))
                            );
                        }


                        result.invoke(new UiState.Success<>(identification));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.invoke(new UiState.Failure("error"));
                    }
                });
    }

    @Override
    public UiState<String> setIdentification(String petName, Identification identification) {
        return null;
    }
}
