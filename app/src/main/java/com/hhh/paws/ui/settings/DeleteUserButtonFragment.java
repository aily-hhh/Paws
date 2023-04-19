package com.hhh.paws.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hhh.paws.R;
import com.hhh.paws.util.FireStoreTables;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.concurrent.TimeUnit;


public class DeleteUserButtonFragment extends Preference {

    Button deleteUser;
    View.OnClickListener clickListener;

    public DeleteUserButtonFragment(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        deleteUser = (Button)holder.findViewById(R.id.deleteUserButton);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setIcon(R.mipmap.logo_paws);
                alertDialog.setTitle(R.string.delete_profile);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                        FirebaseFirestore.getInstance().collection(FireStoreTables.USER).document(uID)
                                .collection(FireStoreTables.PET)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                            Log.d("DELETE", String.valueOf(queryDocumentSnapshots.getDocuments().get(i).getData()));
                                            FirebaseFirestore.getInstance().collection(FireStoreTables.USER).document(uID)
                                                    .collection(FireStoreTables.PET).document(queryDocumentSnapshots.getDocuments().get(i).getId())
                                                    .collection(FireStoreTables.GALLERY).get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryImagesSnapshots) {
                                                            for (int j = 0; j < queryImagesSnapshots.size(); j++) {
                                                                Log.d("DELETE", String.valueOf(queryImagesSnapshots.getDocuments().get(j).getData()));
                                                                StorageReference deleteItem = storageReference.child("images/"+queryImagesSnapshots.getDocuments().get(j).getId());
                                                                deleteItem.delete();
                                                            }
                                                        }
                                                    });
                                            FirebaseFirestore.getInstance().collection(FireStoreTables.USER).document(uID)
                                                    .collection(FireStoreTables.PET).document(queryDocumentSnapshots.getDocuments().get(i).getId())
                                                    .delete();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("DELETE", "ERROR");
                                    }
                                });

                        FirebaseFirestore.getInstance().collection(FireStoreTables.USER).document(uID)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("DELETE", "USER DELETED");
                                        Toast.makeText(
                                                getContext().getApplicationContext(),
                                                getContext().getString(R.string.deleted),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("DELETE", "ERROR");
                                        Toast.makeText(
                                                getContext().getApplicationContext(),
                                                getContext().getString(R.string.error),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                });
                    }
                });
                alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void setClickListener(View.OnClickListener onClickListener)
    {
        clickListener = onClickListener;
    }
}
