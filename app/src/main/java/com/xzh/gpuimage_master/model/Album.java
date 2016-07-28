package com.xzh.gpuimage_master.model;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {

    private static final long serialVersionUID = 5702699517846159671L;
    public String albumUri;
    public String title;
    public List<PhotoItem> photos;

    public Album() {
    }
    public Album(String title, String uri, List<PhotoItem> photos) {
        this.title = title;
        this.albumUri = uri;
        this.photos = photos;
    }

    @Override
    public int hashCode() {
        if (albumUri == null) {
            return super.hashCode();
        } else {
            return albumUri.hashCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && (o instanceof Album)) {
            return TextUtils.equals(albumUri, ((Album) o).albumUri);
        }
        return false;
    }

}
