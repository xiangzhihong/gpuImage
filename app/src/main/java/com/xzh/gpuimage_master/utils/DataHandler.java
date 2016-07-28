package com.xzh.gpuimage_master.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.xzh.gpuimage_master.GPUImageApplication;
import com.xzh.gpuimage_master.model.FilterEffect;
import com.xzh.gpuimage_master.model.WaterMark;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class DataHandler {
    public static final double UNIT = Math.PI / 180;
    public static final float DIFF_DEGREE = 30.0f;
    public static final float DIFF_DEGREE_INNER = 35.0f;

    public static String convertToBigData(String data) {
        float result = 0.00f;
        String resultStr = null;
        if (data != null) {
            float v;
            try {
                v = Float.parseFloat(data);
            } catch (Exception e) {
                v = 0.00f;
                return 0 + "人民币";
            }

            if (v >= 0.00f && v < 10000.99) {
                result = v;
                resultStr = String.format("%.2f", result);
                resultStr = resultStr.endsWith(".00") ? resultStr.substring(0, resultStr.length() - 3) + "人民币" :
                        String.format("%.2f", result) + "人民币";
            } else if (v > 10001 && v < 1000000.99) {
                result = v / 10000.00f;
                resultStr = String.format("%.2f", result);
                resultStr = resultStr.endsWith(".00") ? resultStr.substring(0, resultStr.length() - 3) + "万人民币" :
                        String.format("%.2f", result) + "万人民币";
            } else {
                result = v / 1000000.00f;
                resultStr = String.format("%.2f", result);
                resultStr = resultStr.endsWith(".00") ? resultStr.substring(0, resultStr.length() - 3) + "百万人民币" :
                        String.format("%.2f", result) + "百万人民币";
            }
        }
        return resultStr;
    }

    public static void limitInput(Editable s, int intPart, int decimalPart) {//限制输入的格式
        String content = s == null ? null : s.toString();
        if (s == null || s.length() == 0) {
            return;
        }
        if (content.length() >= 2) {
            String regex = "[0][0-9]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content.substring(0, 2));
            if (matcher.matches()) {
                s.delete(0, 1);
            }
        }

        int size = content.length();
        if (content.contains(".")) {
            if (content.length() - content.indexOf(".") > decimalPart + 1) {
                s.delete(size - 1, size);
            }
        } else {
            if (size > intPart) {
                s.delete(size - 1, size);
            }
        }

        if (!content.endsWith(".")) { //最后输入的不是点，无需处理
            return;
        }

        if (content.substring(0, size - 1).contains(".")) { //判断之前有没有输入过点
            s.delete(size - 1, size);//之前有输入过点，删除重复输入的点
        }
    }

    public static void cacheData(String key, String tagVal) {
        String temp = SharedPreferencesUtil.getString(key, "");
        if (TextUtils.isEmpty(temp)) {
            SharedPreferencesUtil.saveString(key, tagVal);
        } else {
            String[] split = temp.split(",");
            if (split.length == 5) {
                int i = temp.indexOf(",");
                temp = temp.substring(i + 1, temp.length());
            }
            SharedPreferencesUtil.saveString(key, temp + "," + tagVal);
        }
    }

    public static List<String> getCache(String key) {
        String cacheStr = SharedPreferencesUtil.getString(key, "");
        if (!TextUtils.isEmpty(cacheStr)) {
            String[] split = cacheStr.split(",");
            List<String> cacheList = Arrays.asList(split);
            return cacheList;
        }
        return new ArrayList<>();
    }

    public static <T> List<T> StringToList(String asString, Class clazz) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(asString).getAsJsonArray();
        List<T> resultList = JsonUtil.fromJsonToList(jsonArray, clazz);
        return resultList;
    }


    public static boolean isLoad(String lastTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lastTimeMill = 0;
        try {
            Date lastDate = format.parse(lastTime);
            lastTimeMill = lastDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.abs(lastTimeMill - System.currentTimeMillis()) > 24 * 60 * 60 * 1000L;
    }


    public static List<WaterMark> markList = new ArrayList<>();

    static {
        markList.clear();
        String[] list = null;
        try {
            list = GPUImageApplication.getInstance().getAssets().list("waterMark");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.length; i++) {
            String url = "assets://waterMark/%s";
            String format = String.format(url, list[i]);
            WaterMark mark = new WaterMark(format);
            markList.add(mark);
        }
    }

    public static List<FilterEffect> filters = new ArrayList<FilterEffect>();

    static {
        filters.clear();
        filters.add(new FilterEffect("原图", GPUImageFilterTools.FilterType.NORMAL, 0));
        filters.add(new FilterEffect("绮丽", GPUImageFilterTools.FilterType.ACV_GAOLENG, 0));
        filters.add(new FilterEffect("浅滩", GPUImageFilterTools.FilterType.ACV_QINGXIN, 0));
        filters.add(new FilterEffect("和风", GPUImageFilterTools.FilterType.ACV_WENNUAN, 0));
        filters.add(new FilterEffect("可可", GPUImageFilterTools.FilterType.ACV_DANHUANG, 0));
        filters.add(new FilterEffect("午后", GPUImageFilterTools.FilterType.ACV_MORENJIAQIANG, 0));
        filters.add(new FilterEffect("留声", GPUImageFilterTools.FilterType.ACV_AIMEI, 0));
    }


    public static List<Bitmap> getSmallPic(Context context, Bitmap bitmap) {
        List<Bitmap> filterBitmap = new ArrayList<>();
        GPUImage gpuImage = new GPUImage(context);
        for (FilterEffect effect : DataHandler.filters) {
            gpuImage.deleteImage();
            GPUImageFilter filter = GPUImageFilterTools.createFilterForType(context, effect.getType());
            gpuImage.setFilter(filter);
            gpuImage.setImage(bitmap);
            filterBitmap.add(gpuImage.getBitmapWithFilterApplied());
        }
        return filterBitmap;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static boolean collectionNotEmpty(Collection<?> collection) {
        return (collection != null && collection.size() >= 0);
    }

}


