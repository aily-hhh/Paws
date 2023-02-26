package com.hhh.paws.ui.settings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
        deleteUser.setOnClickListener(clickListener);
    }

    public void setClickListener(View.OnClickListener onClickListener)
    {
        clickListener = onClickListener;
    }
}
