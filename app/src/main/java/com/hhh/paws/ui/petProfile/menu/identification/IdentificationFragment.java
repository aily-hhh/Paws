package com.hhh.paws.ui.petProfile.menu.identification;

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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hhh.paws.R;
import com.hhh.paws.database.model.Identification;
import com.hhh.paws.database.viewModel.IdentificationViewModel;
import com.hhh.paws.databinding.FragmentIdentificationBinding;
import com.hhh.paws.util.UiState;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class IdentificationFragment extends Fragment {

    private FragmentIdentificationBinding _binding = null;
    private FragmentIdentificationBinding getBinding() {
        return _binding;
    }

    private TextInputEditText microchipNumber;
    private TextInputEditText dateOfMicrochipping;
    private TextInputEditText microchipLocation;
    private TextInputEditText tattooNumber;
    private TextInputEditText dateOfTattooing;

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

        microchipNumber = getBinding().microchipNumber;
        dateOfMicrochipping = getBinding().dateOfMicrochipping;
        microchipLocation = getBinding().microchipLocation;
        tattooNumber = getBinding().tattooNumber;
        dateOfTattooing = getBinding().dateOfTattooing;

        viewModelIdentification.getIdentification(petNameThis);
        viewModelIdentification.getIdentification
                .observe(getViewLifecycleOwner(), new Observer<UiState<Identification>>() {
            @Override
            public void onChanged(UiState<Identification> identificationUiState) {
                if (identificationUiState == UiState.Loading.INSTANCE) {
                    Toast.makeText(requireContext(), "loading..", Toast.LENGTH_SHORT).show();
                } else if (identificationUiState.getClass() == UiState.Success.class) {
                    microchipLocation.setText(
                            ((UiState.Success<Identification>) identificationUiState)
                                    .getData().getMicrochipLocation()
                    );
                    dateOfMicrochipping.setText(
                            ((UiState.Success<Identification>) identificationUiState)
                                    .getData().getDateOfMicrochipping()
                    );
                    microchipNumber.setText(
                            ((UiState.Success<Identification>) identificationUiState)
                                    .getData().getMicrochipNumber()
                    );
                    tattooNumber.setText(
                            ((UiState.Success<Identification>) identificationUiState)
                                    .getData().getTattooNumber()
                    );
                    dateOfTattooing.setText(
                            ((UiState.Success<Identification>) identificationUiState)
                                    .getData().getDateOfTattooing()
                    );
                } else if (identificationUiState.getClass() == UiState.Failure.class) {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModelIdentification.setIdentification.observe(getViewLifecycleOwner(), new Observer<UiState<String>>() {
            @Override
            public void onChanged(UiState<String> stringUiState) {
                if (stringUiState == UiState.Loading.INSTANCE) {
                    Toast.makeText(requireContext(), "loading..", Toast.LENGTH_SHORT).show();
                } else if (stringUiState.getClass() == UiState.Success.class) {
                    Toast.makeText(
                            requireContext(),
                            ((UiState.Success<String>) stringUiState).getData(),
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (stringUiState.getClass() == UiState.Failure.class) {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}