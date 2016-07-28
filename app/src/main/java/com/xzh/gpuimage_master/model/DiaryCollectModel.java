package com.xzh.gpuimage_master.model;


import com.xzh.gpuimage_master.base.BaseResult;

public class DiaryCollectModel extends BaseResult<DiaryCollectModel.Data> {
    public class Data{
        public boolean IsFav = false;
        public boolean IsSuccess = false;
        public long FavCount = 0;
    }
}
