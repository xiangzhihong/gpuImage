package com.xzh.gpuimage_master.model;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "country_table")
public class Country implements Serializable {

    @DatabaseField(id = true, columnName = "CountryId")
    public int CountryId;

    @DatabaseField(columnName = "CountryName")
    public String CountryName;

    @DatabaseField(columnName = "GroupId")
    public int GroupId;

    @DatabaseField(columnName = "CountryNameZh")
    public String CountryNameZh;

    @DatabaseField(columnName = "Continent")
    public String Continent;

    @DatabaseField(columnName = "Flag")
    public String Flag;
    @DatabaseField(columnName = "TimeZone")
    public String TimeZone;

    @DatabaseField(columnName = "TimeDifference")
    public String TimeDifference;

    @DatabaseField(columnName = "CountryCodeHb")
    public String CountryCodeHb;

    @DatabaseField(columnName = "CountryPhoneCode")
    public String CountryPhoneCode;

    public int getGroupId() {
        int groupId = 0;
        //亚洲-欧洲-大洋洲-北美洲-南美洲-非洲
        if (TextUtils.equals(Continent, "亚洲")) {
            groupId = 0;
        }
        if (TextUtils.equals(Continent, "欧洲")) {
            groupId = 1;
        }
        if (TextUtils.equals(Continent, "大洋洲")) {
            groupId = 2;
        }
        if (TextUtils.equals(Continent, "北美洲")) {
            groupId = 3;
        }
        if (TextUtils.equals(Continent, "美洲")) {
            groupId = 4;
        }
        if (TextUtils.equals(Continent, "南美洲")) {
            groupId = 5;
        }
        if (TextUtils.equals(Continent, "非洲")) {
            groupId = 6;
        }
        return groupId;
    }
}