package com.hhh.paws.database.dao;

import com.hhh.paws.database.model.SurgicalProcedure;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface ProcedureDao {
    Observable<List<SurgicalProcedure>> getAllProcedures(String petName);
    boolean setProcedure(String petName, SurgicalProcedure procedure);
    void deleteProcedure(String petName, SurgicalProcedure procedure);
}
