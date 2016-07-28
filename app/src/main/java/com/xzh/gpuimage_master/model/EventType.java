package com.xzh.gpuimage_master.model;

public class EventType {
    public float type;
    public String tag;
    private TagInfo tagInfo;

    public EventType(int type) {
        this.type = type;
    }

    public EventType(float type, String tag) {
        this.type = type;
        this.tag = tag;
    }


    public float getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagInfo getItem() {
        return tagInfo;
    }

    public void setItem( TagInfo tagInfo) {
        this.tagInfo = tagInfo;
    }
}