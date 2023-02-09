package com.hhh.paws.database.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hhh.paws.database.dao.ProcedureDao;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.util.FireStoreTables;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ProcedureRepository implements ProcedureDao {

    private final FirebaseFirestore database;
    @Inject
    public ProcedureRepository(FirebaseFirestore database) {
        this.database = database;
    }

    @Override
    public Observable<List<SurgicalProcedure>> getAllProcedures(String petName) {
        return Observable.create( emitter -> {
            String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            database.collection(FireStoreTables.USER).document(uID).collection(FireStoreTables.PET)
                    .document(petName).collection(FireStoreTables.PROCEDURES).get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            emitter.onError(e);
                        }
                    }).addOnSuccessListener( documents -> {
                        List<SurgicalProcedure> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document: documents) {
                            SurgicalProcedure procedure = new SurgicalProcedure();
                            procedure.setId(document.getId());
                            procedure.setName(String.valueOf(document.getData().get("name")));
                            procedure.setType(String.valueOf(document.getData().get("type")));
                            procedure.setVeterinarian(String.valueOf(document.getData().get("veterinarian")));
                            procedure.setDescription(String.valueOf(document.getData().get("description")));
                            procedure.setDate(String.valueOf(document.getData().get("date")));
                            procedure.setAnesthesia(String.valueOf(document.getData().get("anesthesia")));
                            list.add(procedure);
                        }
                        emitter.onNext(list);
                        emitter.onComplete();
                    });
        });
    }

    @Override
    public void setProcedure(String petName, SurgicalProcedure procedure) {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection(FireStoreTables.USER).document(uID)
                .collection(FireStoreTables.PET).document(petName)
                .collection(FireStoreTables.PROCEDURES).document(procedure.getId())
                .set(procedure)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void deleteProcedure(String petName, SurgicalProcedure procedure) {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.collection(FireStoreTables.USER).document(uID)
                .collection(FireStoreTables.PET).document(petName)
                .collection(FireStoreTables.PROCEDURES).document(procedure.getId())
                .delete();
    }
}
