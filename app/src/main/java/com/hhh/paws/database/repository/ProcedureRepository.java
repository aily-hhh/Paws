package com.hhh.paws.database.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hhh.paws.database.dao.ProcedureDao;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.util.FireStoreTables;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ProcedureRepository implements ProcedureDao {

    private final FirebaseFirestore database;
    @Inject
    public ProcedureRepository(FirebaseFirestore database) {
        this.database = database;
    }

    @Override
    public Observable<SurgicalProcedure> getAllProcedures(String petName) {
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
                        for (QueryDocumentSnapshot document: documents) {
                            SurgicalProcedure procedure = document.toObject(SurgicalProcedure.class);
                            emitter.onNext(procedure);
                        }
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
                .set(procedure);
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
