package com.hhh.paws.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Gallery implements Parcelable {
    ArrayList<String> galleryList = new ArrayList<>();
    int position;

    public Gallery() {}

    protected Gallery(Parcel in) {
        galleryList = in.createStringArrayList();
        position = in.readInt();
    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel in) {
            return new Gallery(in);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };

    public ArrayList<String> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(ArrayList<String> galleryList) {
        this.galleryList = galleryList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringList(galleryList);
        dest.writeInt(position);
    }
}
