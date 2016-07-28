package com.xzh.gpuimage_master.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "category_table")
public class Category implements Serializable {

    @DatabaseField(columnName = "CategoryId", id = true)
    public int CategoryId;

    @DatabaseField(columnName = "CategoryName")
    public String CategoryName;

    @DatabaseField(columnName = "IsHasAct")
    public String IsHasAct;

    @DatabaseField(columnName = "Desc")
    public String Desc;
}