package com.hhh.paws.ui.petProfile.menu.identification;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hhh.paws.R;
import com.hhh.paws.database.model.Identification;
import com.hhh.paws.database.viewModel.IdentificationViewModel;
import com.hhh.paws.databinding.FragmentIdentificationBinding;
import com.hhh.paws.util.UiState;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@AndroidEntryPoint
public class IdentificationFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentIdentificationBinding _binding = null;
    private FragmentIdentificationBinding getBinding() {
        return _binding;
    }

    private TextInputEditText microchipNumber;
    private TextInputEditText dateOfMicrochipping;
    private TextInputEditText microchipLocation;
    private TextInputEditText tattooNumber;
    private TextInputEditText dateOfTattooing;
    private ProgressBar progressBarIdentification;

    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int savedDay = 0;
    private int savedMonth = 0;
    private int savedYear = 0;
    private int flag = 0;
    private static final int DATE_TATTOO = 1;
    private static final int DATE_CHIP = 2;

    private Disposable disposableGet;
    private IdentificationViewModel viewModelIdentification;
    private String petNameThis;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentIdentificationBinding.inflate(inflater, container, false);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModelIdentification = new ViewModelProvider(
                IdentificationFragment.this).get(IdentificationViewModel.class
        );

        petNameThis = "Котик";

        progressBarIdentification = getBinding().progressBarIdentification;
        progressBarIdentification.setVisibility(View.VISIBLE);

        microchipNumber = getBinding().microchipNumber;
        microchipLocation = getBinding().microchipLocation;
        tattooNumber = getBinding().tattooNumber;

        dateOfTattooing = getBinding().dateOfTattooing;
        dateOfTattooing.setOnClickListener(v -> {
            flag = DATE_TATTOO;
            getDateCalendar();
            new DatePickerDialog(requireContext(), this, year, month, day).show();
        });

        dateOfMicrochipping = getBinding().dateOfMicrochipping;
        dateOfMicrochipping.setOnClickListener(v -> {
            flag = DATE_CHIP;
            getDateCalendar();
            new DatePickerDialog(requireContext(), this, year, month, day).show();
        });

        disposableGet = viewModelIdentification.getIdentification(petNameThis)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( it -> {
                    progressBarIdentification.setVisibility(View.INVISIBLE);
                    microchipNumber.setText(it.getMicrochipNumber());
                    dateOfMicrochipping.setText(it.getDateOfMicrochipping());
                    microchipLocation.setText(it.getMicrochipLocation());
                    tattooNumber.setText(it.getTattooNumber());
                    dateOfTattooing.setText(it.getDateOfTattooing());
                    }, it -> {
                        progressBarIdentification.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                );
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            Identification newIdentification = new Identification();
            newIdentification.setTattooNumber(tattooNumber.getText().toString().trim());
            newIdentification.setMicrochipNumber(microchipNumber.getText().toString().trim());
            newIdentification.setMicrochipLocation(microchipLocation.getText().toString().trim());
            newIdentification.setDateOfMicrochipping(dateOfMicrochipping.getText().toString().trim());
            newIdentification.setDateOfTattooing(dateOfTattooing.getText().toString().trim());

            viewModelIdentification.setIdentification(petNameThis, newIdentification);

            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposableGet.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }

    private void getDateCalendar() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        savedDay = dayOfMonth;
        savedMonth = month + 1;
        savedYear = year;

        if (flag == DATE_TATTOO) {
            dateOfTattooing.setText(savedDay + "." + savedMonth + "." + savedYear);
        } else if (flag == DATE_CHIP) {
            dateOfMicrochipping.setText(savedDay + "." + savedMonth + "." + savedYear);
        }

        flag = 0;
    }
}