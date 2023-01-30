package com.hhh.paws.database.model;

public class Identification {

    String microchipNumber;
    String dateOfMicrochipping;
    String microchipLocation;
    String tattooNumber;
    String dateOfTattooing;

    public String getMicrochipNumber() {
        return microchipNumber;
    }

    public void setMicrochipNumber(String microchipNumber) {
        this.microchipNumber = microchipNumber;
    }

    public String getDateOfMicrochipping() {
        return dateOfMicrochipping;
    }

    public void setDateOfMicrochipping(String dateOfMicrochipping) {
        this.dateOfMicrochipping = dateOfMicrochipping;
    }

    public String getMicrochipLocation() {
        return microchipLocation;
    }

    public void setMicrochipLocation(String microchipLocation) {
        this.microchipLocation = microchipLocation;
    }

    public String getTattooNumber() {
        return tattooNumber;
    }

    public void setTattooNumber(String tattooNumber) {
        this.tattooNumber = tattooNumber;
    }

    public String getDateOfTattooing() {
        return dateOfTattooing;
    }

    public void setDateOfTattooing(String dateOfTattooing) {
        this.dateOfTattooing = dateOfTattooing;
    }
}
