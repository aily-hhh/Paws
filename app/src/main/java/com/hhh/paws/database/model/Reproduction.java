package com.hhh.paws.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Reproduction implements Parcelable {
    String id;
    String dateOfHeat;
    String dateOfMating;
    String dateOfBirth;
    String numberOfTheLitter;

    public Reproduction(){}

    protected Reproduction(Parcel in) {
        id = in.readString();
        dateOfHeat = in.readString();
        dateOfMating = in.readString();
        dateOfBirth = in.readString();
        numberOfTheLitter = in.readString();
    }

    public static final Creator<Reproduction> CREATOR = new Creator<Reproduction>() {
        @Override
        public Reproduction createFromParcel(Parcel in) {
            return new Reproduction(in);
        }

        @Override
        public Reproduction[] newArray(int size) {
            return new Reproduction[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateOfHeat() {
        return dateOfHeat;
    }

    public void setDateOfHeat(String dateOfHeat) {
        this.dateOfHeat = dateOfHeat;
    }

    public String getDateOfMating() {
        return dateOfMating;
    }

    public void setDateOfMating(String dateOfMating) {
        this.dateOfMating = dateOfMating;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNumberOfTheLitter() {
        return numberOfTheLitter;
    }

    public void setNumberOfTheLitter(String numberOfTheLitter) {
        this.numberOfTheLitter = numberOfTheLitter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(dateOfBirth);
        dest.writeString(dateOfHeat);
        dest.writeString(dateOfMating);
        dest.writeString(numberOfTheLitter);
    }
}
