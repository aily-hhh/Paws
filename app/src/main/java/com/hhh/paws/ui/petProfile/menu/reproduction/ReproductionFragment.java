package com.hhh.paws.ui.petProfile.menu.reproduction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.viewModel.ReproductionViewModel;
import com.hhh.paws.databinding.FragmentReproductionBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ReproductionFragment extends Fragment {

    private FragmentReproductionBinding _binding = null;
    private FragmentReproductionBinding getBinding() {
        return _binding;
    }

    private RecyclerView recyclerReproduction;
    private FloatingActionButton addReproductionButton;
    private TextView notElemReproduction, addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarReproduction;

    private ReproductionViewModel viewModelReproduction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelReproduction = new ViewModelProvider(ReproductionFragment.this).get(ReproductionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentReproductionBinding.inflate(inflater, container, false);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBarReproduction = getBinding().progressBarReproduction;
        recyclerReproduction = getBinding().recyclerReproduction;
        notElemReproduction = getBinding().notElemReproduction;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;


        addReproductionButton = getBinding().addReproductionButton;
        addReproductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_reproduction_to_detailReproductionFragment);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }
}