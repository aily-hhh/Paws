package com.hhh.paws.database.dao;

import com.hhh.paws.database.model.Identification;
import com.hhh.paws.util.UiState;

import java.util.function.Function;

import io.reactivex.rxjava3.core.Observable;
import kotlin.jvm.functions.Function1;

public interface IdentificationDao {
    Observable<Identification> getIdentification(String petName);
    void setIdentification(String petName, Identification identification);
}
