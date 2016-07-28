package com.xzh.gpuimage_master.model;

import java.io.Serializable;

/**
 * Created by wangzheng on 2015/9/21.
 */
public class ActivityEntity implements Serializable{
    public String activityId = null;
    public String activityName = null;

    public ActivityEntity(String activityId, String activityName) {
        this.activityId = activityId;
        this.activityName = activityName;
    }
}
