package com.hhh.paws.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hhh.paws.MainActivity;
import com.hhh.paws.R;
import com.hhh.paws.util.FireStoreTables;

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
                                        }
                                    }
                                });

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("DELETE", "user deleted");
                                    AuthUI.getInstance().delete(getContext());
                                    System.exit(0);
                                }
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
