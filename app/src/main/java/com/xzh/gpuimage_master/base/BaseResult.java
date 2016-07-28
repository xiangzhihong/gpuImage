package com.xzh.gpuimage_master.base;

/**
 * Created by mark.wu on 15/5/26.
 */
public class BaseResult<T> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public int Status;
    public String Msg;
    public T Result;
    public String ServerTime;
}
