package com.hhh.paws.database.dao;

import com.hhh.paws.database.model.Identification;
import com.hhh.paws.util.UiState;

public interface IdentificationDao {
    void getIdentification(String petName);
    UiState<String> setIdentification(String petName, Identification identification);
}
