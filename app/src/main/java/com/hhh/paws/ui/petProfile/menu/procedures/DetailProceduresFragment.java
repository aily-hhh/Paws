package com.hhh.paws.ui.petProfile.menu.procedures;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavArgs;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.hhh.paws.R;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.database.viewModel.ProcedureViewModel;
import com.hhh.paws.databinding.FragmentProceduresDetailBinding;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.Disposable;


@AndroidEntryPoint
public class DetailProceduresFragment extends Fragment {

    private FragmentProceduresDetailBinding _binding = null;
    private FragmentProceduresDetailBinding mBinding = _binding;

    private ProgressBar progressBarProcedureDetail;
    private MaterialAutoCompleteTextView spinnerTypeSurgicalProcedure;
    private TextInputEditText nameSurgicalProcedure;
    private TextInputEditText anesthesiaSurgicalProcedure;
    private TextInputEditText dateSurgicalProcedure;
    private TextInputEditText veterinarianSurgicalProcedure;
    private TextInputEditText descriptionSurgicalProcedure;

    private ProcedureViewModel viewModelProcedures;
    private String petNameThis;
    private SurgicalProcedure procedure = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentProceduresDetailBinding.inflate(inflater, container, false);
        viewModelProcedures = new ViewModelProvider(
                DetailProceduresFragment.this).get(ProcedureViewModel.class

        );
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petNameThis = "Котик";
        procedure = getArguments().getParcelable("procedure");

        progressBarProcedureDetail = mBinding.progressBarProcedureDetail;
        spinnerTypeSurgicalProcedure = mBinding.spinnerTypeSurgicalProcedure;
        nameSurgicalProcedure = mBinding.nameSurgicalProcedure;
        anesthesiaSurgicalProcedure = mBinding.anesthesiaSurgicalProcedure;
        dateSurgicalProcedure = mBinding.dateSurgicalProcedure;
        veterinarianSurgicalProcedure = mBinding.veterinarianSurgicalProcedure;
        descriptionSurgicalProcedure = mBinding.descriptionSurgicalProcedure;

        if (procedure != null) {
            spinnerTypeSurgicalProcedure.setText(procedure.getType());
            nameSurgicalProcedure.setText(procedure.getName());
            anesthesiaSurgicalProcedure.setText(procedure.getAnesthesia());
            dateSurgicalProcedure.setText(procedure.getDate());
            veterinarianSurgicalProcedure.setText(procedure.getVeterinarian());
            descriptionSurgicalProcedure.setText(procedure.getDescription());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            SurgicalProcedure newProcedure = new SurgicalProcedure();
            if (procedure != null) {
                newProcedure.setId(procedure.getId());
            } else {
                newProcedure.setId(UUID.randomUUID().toString());
            }
            newProcedure.setAnesthesia(anesthesiaSurgicalProcedure.getText().toString());
            newProcedure.setDate(dateSurgicalProcedure.getText().toString());
            newProcedure.setDescription(descriptionSurgicalProcedure.getText().toString());
            newProcedure.setVeterinarian(veterinarianSurgicalProcedure.getText().toString());
            newProcedure.setType(spinnerTypeSurgicalProcedure.getText().toString());
            newProcedure.setName(nameSurgicalProcedure.getText().toString());

            viewModelProcedures.setProcedure(petNameThis, newProcedure);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}