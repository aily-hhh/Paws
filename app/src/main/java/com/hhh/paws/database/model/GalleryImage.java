package com.hhh.paws.database.model;

import android.net.Uri;

public class GalleryImage {
    Uri id;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getId() {
        return id;
    }

    public void setId(Uri id) {
        this.id = id;
    }
}
