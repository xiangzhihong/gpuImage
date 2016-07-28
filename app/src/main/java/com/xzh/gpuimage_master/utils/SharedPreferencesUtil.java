package com.xzh.gpuimage_master.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xzh.gpuimage_master.GPUImageApplication;

public class SharedPreferencesUtil {

    public static final String DEFAULT_SHARE_NODE = "ymatou_seller_sharepreference";

    public static void saveInt(String node, String key, int value) {
        SharedPreferences sp = GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveInt(String key, int value) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(DEFAULT_SHARE_NODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveLong(String node, String key, long value) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void saveBoolean(String node, String key, boolean value) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(DEFAULT_SHARE_NODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveString(String node, String key, String value) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveString(String key, String value) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(DEFAULT_SHARE_NODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getInt(String node, String key, int defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultvalue);
    }

    public static int getInt(String key, int defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(DEFAULT_SHARE_NODE, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultvalue);
    }

    public static long getLong(String node, String key, int defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultvalue);
    }


    public static boolean getBoolean(String node, String key, boolean defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultvalue);
    }

    public static boolean getBoolean(String key, boolean defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(DEFAULT_SHARE_NODE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultvalue);
    }

    public static String getString(String node, String key, String defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(node, Context.MODE_PRIVATE);
        return sp.getString(key, defaultvalue);
    }

    public static String getString(String key, String defaultvalue) {
        SharedPreferences sp =  GPUImageApplication.getInstance().getSharedPreferences(DEFAULT_SHARE_NODE, Context.MODE_PRIVATE);
        return sp.getString(key, defaultvalue);
    }
}