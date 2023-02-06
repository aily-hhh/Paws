package com.hhh.paws.database.dao;

import com.hhh.paws.database.model.SurgicalProcedure;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface ProcedureDao {
    Observable<SurgicalProcedure> getAllProcedures(String petName);
    void setProcedure(String petName, SurgicalProcedure procedure);
    void deleteProcedure(String petName, SurgicalProcedure procedure);
}
