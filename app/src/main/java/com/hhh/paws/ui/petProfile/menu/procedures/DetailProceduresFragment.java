package com.hhh.paws.ui.petProfile.menu.procedures;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.hhh.paws.R;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.database.viewModel.ProcedureViewModel;
import com.hhh.paws.databinding.FragmentDetailProceduresBinding;

import java.util.Calendar;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DetailProceduresFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentDetailProceduresBinding _binding = null;
    private FragmentDetailProceduresBinding getBinding() {
        return _binding;
    }

    private MaterialAutoCompleteTextView spinnerTypeSurgicalProcedure;
    private TextInputEditText nameSurgicalProcedure;
    private TextInputEditText anesthesiaSurgicalProcedure;
    private TextInputEditText dateSurgicalProcedure;
    private TextInputEditText veterinarianSurgicalProcedure;
    private TextInputEditText descriptionSurgicalProcedure;
    private ProgressBar progressBarProcedureDetail;

    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int savedDay = 0;
    private int savedMonth = 0;
    private int savedYear = 0;

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
        _binding = FragmentDetailProceduresBinding.inflate(inflater, container, false);
        viewModelProcedures = new ViewModelProvider(
                DetailProceduresFragment.this).get(ProcedureViewModel.class

        );
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petNameThis = "Котик";
        procedure = getArguments().getParcelable("procedure");

        progressBarProcedureDetail = getBinding().progressBarProcedureDetail;
        spinnerTypeSurgicalProcedure = getBinding().spinnerTypeSurgicalProcedure;
        nameSurgicalProcedure = getBinding().nameSurgicalProcedure;
        anesthesiaSurgicalProcedure = getBinding().anesthesiaSurgicalProcedure;
        veterinarianSurgicalProcedure = getBinding().veterinarianSurgicalProcedure;
        descriptionSurgicalProcedure = getBinding().descriptionSurgicalProcedure;

        dateSurgicalProcedure = getBinding().dateSurgicalProcedure;
        dateSurgicalProcedure.setOnClickListener(v -> {
            getDateSet();
            new DatePickerDialog(requireContext(), this, year, month, day).show();
        });

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
            progressBarProcedureDetail.setVisibility(View.VISIBLE);
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

            progressBarProcedureDetail.setVisibility(View.INVISIBLE);
            viewModelProcedures.setProcedure(petNameThis, newProcedure);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .popBackStack();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }

    private void getDateSet() {
        day = Calendar.DAY_OF_WEEK_IN_MONTH;
        month = Calendar.MONTH;
        year = Calendar.YEAR;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        savedDay = dayOfMonth;
        savedMonth = month + 1;
        savedYear = year;

        dateSurgicalProcedure.setText(savedDay + "." + savedMonth + "." + savedYear);
    }
}