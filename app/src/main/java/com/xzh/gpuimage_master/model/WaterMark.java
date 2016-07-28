package com.xzh.gpuimage_master.model;

public class WaterMark {

    public WaterMark(String uri) {
        this.uri = uri;
    }

    private String name;
    private String uri;
    private int id;

    public String getName() {
        int index = 0;
        try {
            index = uri.lastIndexOf("/");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return uri.substring(index-1, uri.length());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
