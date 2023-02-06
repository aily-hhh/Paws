package com.hhh.paws.database.viewModel;

import androidx.lifecycle.ViewModel;

import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.database.repository.ProcedureRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;


@HiltViewModel
public class ProcedureViewModel extends ViewModel {

    private final ProcedureRepository repository;
    @Inject
    public ProcedureViewModel(ProcedureRepository repository) {
        this.repository = repository;
    }

    public Observable<SurgicalProcedure> getAllProcedures(String petName) {
        return repository.getAllProcedures(petName);
    }

    public void setProcedure(String petName, SurgicalProcedure procedure) {
        repository.setProcedure(petName, procedure);
    }

    public void deleteProcedure(String petName, SurgicalProcedure procedure) {
        repository.deleteProcedure(petName, procedure);
    }
}
