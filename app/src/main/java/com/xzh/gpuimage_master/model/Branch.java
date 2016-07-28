package com.xzh.gpuimage_master.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table_branch")
public class Branch {
    @DatabaseField(id = true, columnName = "BrandId")
    public int BrandId;

    @DatabaseField(columnName = "BrandName")
    public String BrandName;

    @DatabaseField(columnName = "BrandAlias")
    public String BrandAlias;

    @DatabaseField(columnName = "Key")
    public String Key;
}