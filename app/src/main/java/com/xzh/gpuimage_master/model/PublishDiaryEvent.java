package com.xzh.gpuimage_master.model;

public class PublishDiaryEvent {
    public PublishDiaryEvent(String diaryId, int state) {
        this.diaryId = diaryId;
        this.state = state;
    }

    public String diaryId = null;
    public String imagePath = null;
    public int progress = 0;
    public int state = 0;//-1发布失败，0成功，1正在发布,2取消发布,3删除笔记
}
