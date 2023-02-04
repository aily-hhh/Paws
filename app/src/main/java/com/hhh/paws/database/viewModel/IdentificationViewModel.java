package com.hhh.paws.database.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hhh.paws.database.model.Identification;
import com.hhh.paws.database.repository.IdentificationRepository;
import com.hhh.paws.util.UiState;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class IdentificationViewModel extends ViewModel {

    private final IdentificationRepository repository;
    @Inject
    public IdentificationViewModel(IdentificationRepository repository) {
        this.repository = repository;
    }

    private MutableLiveData<UiState<Identification>> _getIdentification = new MutableLiveData<>();
    public LiveData<UiState<Identification>> getIdentification = _getIdentification;
    public void getIdentification(String petName) {
        _getIdentification.setValue(UiState.Loading.INSTANCE);
        repository.getIdentification(petName, it -> {
            _getIdentification.setValue((UiState<Identification>) it);
            return _getIdentification;
        });
    }

    private MutableLiveData<UiState<String>> _setIdentification = new MutableLiveData<>();
    public LiveData<UiState<String>> setIdentification = _setIdentification;
    public void setIdentification(String petName, Identification identification) {
        _setIdentification.setValue(UiState.Loading.INSTANCE);
        repository.setIdentification(petName, identification, it -> {
            _setIdentification.setValue((UiState<String>) it);
            return setIdentification;
        });
    }

}
