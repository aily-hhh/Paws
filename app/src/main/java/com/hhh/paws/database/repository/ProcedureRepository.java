package com.hhh.paws.database.repository;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hhh.paws.database.dao.ProcedureDao;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.util.FireStoreTables;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProcedureRepository implements ProcedureDao {

    private final FirebaseFirestore database;
    @Inject
    public ProcedureRepository(FirebaseFirestore database) {
        this.database = database;
    }

    @Override
    public Observable<List<SurgicalProcedure>> getAllProcedures(String petName) {
        return Observable.fromSingle(new Single<List<SurgicalProcedure>>() {
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super List<SurgicalProcedure>> observer) {
                String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
                        .document(petName).collection(FireStoreTables.PROCEDURES).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<SurgicalProcedure> list = new ArrayList<>();
                                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                                    SurgicalProcedure newProcedure = new SurgicalProcedure();
                                    newProcedure.setId(queryDocumentSnapshots.getDocuments().get(i).getId());
                                    newProcedure.setAnesthesia(
                                            queryDocumentSnapshots.getDocuments().get(i)
                                                    .getData().get("anesthesia").toString());
                                    newProcedure.setDate(
                                            queryDocumentSnapshots.getDocuments().get(i)
                                                    .getData().get("date").toString());
                                    newProcedure.setDescription(
                                            queryDocumentSnapshots.getDocuments().get(i)
                                                    .getData().get("description").toString());
                                    newProcedure.setName(
                                            queryDocumentSnapshots.getDocuments().get(i)
                                                    .getData().get("name").toString());
                                    newProcedure.setType(
                                            queryDocumentSnapshots.getDocuments().get(i)
                                                    .getData().get("type").toString());
                                    newProcedure.setVeterinarian(
                                            queryDocumentSnapshots.getDocuments()
                                            .get(i).getData().get("veterinarian").toString());

                                    list.add(newProcedure);
                                }

                                observer.onSuccess(list);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Log.d("RxJava", "Surgical procedure failed");
                            }
                        });
            }
        });
    }

    @Override
    public void setProcedure(String petName, SurgicalProcedure procedure) {
        Disposable dispose = Observable.fromSingle(new Single<SurgicalProcedure>() {
            @Override
            protected void subscribeActual(@io.reactivex.rxjava3.annotations.NonNull SingleObserver<? super SurgicalProcedure> observer) {
                observer.onSuccess(procedure);
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(it -> {
            String uID = FirebaseAuth.getInstance().getUid();

            database.collection(FireStoreTables.USER).document(uID)
                    .collection(FireStoreTables.PET).document(petName)
                    .collection(FireStoreTables.PROCEDURES).document(procedure.getId())
                    .set(it).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firestore", "Surgical procedure success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Log.d("Firestore", "Surgical procedure failed");
                        }
                    });
        }, it -> {
            Log.d("RxJava", "Surgical procedure failed");
        });

        dispose.dispose();
    }

    @Override
    public void deleteProcedure(String petName, SurgicalProcedure procedure) {
        Disposable dispose = Observable.fromSingle(new Single<SurgicalProcedure>() {
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super SurgicalProcedure> observer) {

            }
        }).subscribeOn(Schedulers.newThread()).subscribe(it -> {
            String uID = FirebaseAuth.getInstance().getUid();

            database.collection(FireStoreTables.USER).document(uID)
                    .collection(FireStoreTables.PET).document(petName)
                    .collection(FireStoreTables.PROCEDURES).document(procedure.getId())
                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firestore", "Surgical procedure deleted");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Log.d("Firestore", "Surgical procedure failed");
                        }
                    });
        }, it -> {
            Log.d("RxJava", "Surgical procedure failed");
        });

        dispose.dispose();
    }
}
