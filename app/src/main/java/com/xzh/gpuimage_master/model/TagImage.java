package com.xzh.gpuimage_master.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class TagImage implements Serializable {
    public String Pic = null;
    public String localPath = null;
    public int  filterType=0;
    public List<TagInfo> TagInfo = new LinkedList<>();
}
