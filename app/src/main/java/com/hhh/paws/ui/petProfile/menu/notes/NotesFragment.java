package com.hhh.paws.ui.petProfile.menu.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.databinding.FragmentNotesBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class NotesFragment extends Fragment {

    private RecyclerView recyclerNotes;
    private FloatingActionButton addNotesButton;
    private TextView notElemNotes;

    private FragmentNotesBinding _binding = null;
    private FragmentNotesBinding getBinding() {
        return _binding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentNotesBinding.inflate(inflater, container, false);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerNotes = getBinding().recyclerNotes;
        notElemNotes = getBinding().notElemNotes;

        addNotesButton = getBinding().addNotesButton;
        addNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_notes_to_detailNoteFragment);
            }
        });
    }
}