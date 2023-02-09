package com.hhh.paws.ui.petProfile.menu.vaccines;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.viewModel.VaccinesViewModel;
import com.hhh.paws.databinding.FragmentVaccinesBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class VaccinesFragment extends Fragment {

    private FragmentVaccinesBinding _binding = null;
    private FragmentVaccinesBinding getBinding() {
        return _binding;
    }

    private RecyclerView recyclerVaccines;
    private FloatingActionButton addVaccinesButton;
    private TextView notElemVaccines, addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarVaccines;

    private VaccinesViewModel viewModelVaccines;
    private String petName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding= FragmentVaccinesBinding.inflate(inflater, container, false);
        viewModelVaccines = new ViewModelProvider(VaccinesFragment.this).get(VaccinesViewModel.class);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petName = "Котик";

        recyclerVaccines = getBinding().recyclerVaccines;

        addVaccinesButton = getBinding().addVaccinesButton;
        addVaccinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        notElemVaccines = getBinding().notElemVaccines;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;
        progressBarVaccines = getBinding().progressBarVaccines;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }
}