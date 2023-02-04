package com.hhh.paws.database.dao;

import com.hhh.paws.database.model.Identification;
import com.hhh.paws.util.UiState;

import java.util.function.Function;

import kotlin.jvm.functions.Function1;

public interface IdentificationDao {
    void getIdentification(String petName, final Function1 result);
    void setIdentification(String petName, Identification identification, final Function1 result);
}
