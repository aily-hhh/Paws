package com.hhh.paws.database.repository;

import android.util.Log;

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

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.functions.Function1;


public class IdentificationRepository implements IdentificationDao {

    private final FirebaseFirestore database;
    @Inject
    public IdentificationRepository(FirebaseFirestore database) {
        this.database = database;
    }

    @Override
    public Observable<Identification> getIdentification(String petName) {
        return Observable.create(emitter -> {
            String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            database.collection(FireStoreTables.USER).document(uID)
                    .collection(FireStoreTables.PET).document(petName)
                    .collection(FireStoreTables.IDENTIFICATION).document(petName)
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

                            emitter.onNext(identification);
                            emitter.onComplete();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Identification", e.getLocalizedMessage());
                        }
                    });
        });
    }

    @Override
    public void setIdentification(String petName, Identification identification) {
        String uID = FirebaseAuth.getInstance().getUid();

        database.collection(FireStoreTables.USER).document(uID)
                .collection(FireStoreTables.PET).document(petName)
                .collection(FireStoreTables.IDENTIFICATION).document(petName)
                .set(identification).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Identification success");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Identification failed");
                    }
                });
    }
}
