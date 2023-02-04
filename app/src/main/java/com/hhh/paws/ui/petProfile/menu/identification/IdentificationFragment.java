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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentIdentificationBinding.inflate(inflater, container, false);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IdentificationViewModel viewModelIdentification =
                new ViewModelProvider(IdentificationFragment.this).get(IdentificationViewModel.class);

        microchipNumber = getBinding().microchipNumber;
        dateOfMicrochipping = getBinding().dateOfMicrochipping;
        microchipLocation = getBinding().microchipLocation;
        tattooNumber = getBinding().tattooNumber;
        dateOfTattooing = getBinding().dateOfTattooing;

        viewModelIdentification.getIdent("Котик");
        viewModelIdentification.getIdentification()
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
    }
}