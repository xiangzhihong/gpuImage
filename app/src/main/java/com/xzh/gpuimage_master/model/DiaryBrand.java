package com.xzh.gpuimage_master.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DiaryBrand")
public class DiaryBrand {
    @DatabaseField(columnName = "Key")
    public String Key;

    @DatabaseField(columnName = "BrandId", id = true, canBeNull = false)
    public String BrandId;

    @DatabaseField(columnName = "BrandNameZH")
    public String BrandNameZH;

    @DatabaseField(columnName = "BrandName")
    public String BrandName;

    @DatabaseField(columnName = "BrandAlias")
    public String BrandAlias;

    @DatabaseField(columnName = "DescriptionZH")
    public String DescriptionZH;

    @DatabaseField(columnName = "DescriptionEN")
    public String DescriptionEN;

    @DatabaseField(columnName = "Sort")
    public String Sort;
}
