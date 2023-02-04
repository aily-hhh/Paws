package com.hhh.paws.database.viewModel;

import androidx.lifecycle.ViewModel;

import com.hhh.paws.database.model.Identification;
import com.hhh.paws.database.repository.IdentificationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;


@HiltViewModel
public class IdentificationViewModel extends ViewModel {

    private IdentificationRepository repository;
    @Inject
    public IdentificationViewModel(IdentificationRepository repository) {
        this.repository = repository;
    }

    public Observable<Identification> getIdentification(String petName) {
        return repository.getIdentification(petName);
    }

    public void setIdentification(String petName, Identification identification) {
        repository.setIdentification(petName, identification);
    }
}
