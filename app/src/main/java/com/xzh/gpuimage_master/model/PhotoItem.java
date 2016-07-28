package com.xzh.gpuimage_master.model;

import android.os.Parcel;

import java.io.Serializable;


public class PhotoItem implements Serializable, Comparable<PhotoItem> {

    public String imageUri;
    public long date;
    public boolean checked;
    public String dateStr;
    public boolean uploaded;

    public PhotoItem(String uri, long date) {
        this.imageUri = uri;
        this.date = date;
        this.uploaded = false;
    }

    public PhotoItem(Parcel in) {
        imageUri = in.readString();
        date = in.readLong();
    }

    @Override
    public int compareTo(PhotoItem another) {
        if (another == null) {
            return 1;
        }
        return (int) ((another.date - date) / 1000);
    }
}
