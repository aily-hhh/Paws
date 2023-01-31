package com.hhh.paws.database.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hhh.paws.database.model.Identification;
import com.hhh.paws.database.repository.IdentificationRepository;
import com.hhh.paws.util.UiState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.jvm.functions.Function1;

@HiltViewModel
public class IdentificationViewModel extends ViewModel {

    private final IdentificationRepository repository;
    @Inject
    public IdentificationViewModel(IdentificationRepository repository) {
        this.repository = repository;
    }

    private MutableLiveData<UiState<Identification>> _getIdentification = new MutableLiveData<>();
    public LiveData<UiState<Identification>> getIdentification() {
        return _getIdentification;
    }
    public void getIdent(String petName) {
        _getIdentification.setValue(UiState.Loading.INSTANCE);
        repository.getIdentification(petName, it -> {
            _getIdentification.setValue((UiState<Identification>) it);
            return _getIdentification;
        });
    }
}
