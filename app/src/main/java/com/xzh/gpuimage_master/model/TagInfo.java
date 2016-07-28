package com.xzh.gpuimage_master.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TagInfo implements Serializable {
    public float XPoint;
    public float YPoint;
    public int ImageTagStyle = 1;
    public float firstPointX;
    public List<Tag> Tags = new LinkedList<>();


}
