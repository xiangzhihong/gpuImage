package com.xzh.gpuimage_master.model;

import java.io.Serializable;

public class Tag implements Serializable {
    public String TagValId = null;
    public String TagVal = null;
    public int ImageTagType = -1;

    public Tag(String TagValId, String TagVal, int ImageTagType) {
        this.ImageTagType = ImageTagType;
        this.TagVal = TagVal;
        this.TagValId = TagValId;
    }

    public Tag() {
    }
}
