package com.xzh.gpuimage_master.model;

public class DiaryFavUser {
    private int UserId;
    private String UserName;
    private String UserLogo;
    private int UserType;
    private String CountryName;
    private String Flag;
    private int FansNum;
    private int ActivityStatus;
    private boolean IsReply;
    private int DiaryNum;
    private int FollowType;
    private int UserFollowType;
    private String AddTime;
    private long LongPostTime;

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setUserLogo(String UserLogo) {
        this.UserLogo = UserLogo;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    public void setFlag(String Flag) {
        this.Flag = Flag;
    }

    public void setFansNum(int FansNum) {
        this.FansNum = FansNum;
    }

    public void setActivityStatus(int ActivityStatus) {
        this.ActivityStatus = ActivityStatus;
    }

    public void setIsReply(boolean IsReply) {
        this.IsReply = IsReply;
    }

    public void setDiaryNum(int DiaryNum) {
        this.DiaryNum = DiaryNum;
    }

    public void setFollowType(int FollowType) {
        this.FollowType = FollowType;
    }

    public void setUserFollowType(int UserFollowType) {
        this.UserFollowType = UserFollowType;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public void setLongPostTime(long LongPostTime) {
        this.LongPostTime = LongPostTime;
    }

    public int getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserLogo() {
        return UserLogo;
    }

    public int getUserType() {
        return UserType;
    }

    public String getCountryName() {
        return CountryName;
    }

    public String getFlag() {
        return Flag;
    }

    public int getFansNum() {
        return FansNum;
    }

    public int getActivityStatus() {
        return ActivityStatus;
    }

    public boolean isIsReply() {
        return IsReply;
    }

    public int getDiaryNum() {
        return DiaryNum;
    }

    public int getFollowType() {
        return FollowType;
    }

    public int getUserFollowType() {
        return UserFollowType;
    }

    public String getAddTime() {
        return AddTime;
    }

    public long getLongPostTime() {
        return LongPostTime;
    }
}
