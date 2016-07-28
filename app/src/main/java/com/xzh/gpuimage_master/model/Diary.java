package com.xzh.gpuimage_master.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Diary implements Serializable {
    public String NoteId = System.currentTimeMillis() + "";
    public String Os = "android";
    public String Ip = null;
    public String OsVer = android.os.Build.VERSION.RELEASE;
    public String ActivityId = null;
    public String CkId = null;
    public String Imei = null;
    public String OrderId = null;
    public String Position = null;
    @SerializedName("TagImage")
    public List<TagImage> TagImages = new LinkedList<>();
    public String UserId = null;
    public String UserName = null;
    public String SellerNewsId = null;
    public String Idfa = null;
    public String Content = null;
    public String ActivityName = null;
    public int NoteSource = 3;//日记来源，0：买家添加，1：历史数据，2：运营添加，3：买手添加
    public String NoteVersion = null;

    //详情扩展
    public String ProductDes = null;
    public String ProductPic = null;
    public int Status = 0;//0：审核通过   1：待审核  2：审核未通过
    public List<Tag> CustomTags = null;
    //本地扩展
    public boolean isUpdateDiry = false;
    public String SellerId;
    public boolean isDelete;
}
