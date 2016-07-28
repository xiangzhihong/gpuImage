package com.xzh.gpuimage_master.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public final class DateUtil {

    public static String formatDate(String str,String pattern) {
        String fromatStr=null;
        Date date= toDate(str);
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        fromatStr=df.format(date);
        return fromatStr;
    }

    public static Date toDate(String str) {
        String pattern =null;
        SimpleDateFormat format = null;
        try {
            if (str.contains(".")) {
                if (str.contains(" ") && str.contains(":"))
                    pattern = "yyyy.MM.dd HH:mm:ss";
                else
                    pattern = "yyyy.MM.dd";
            } else if (str.contains("-")) {
                if (str.contains(" ") && str.contains(":"))
                    if(str.split(":").length>1){
                        pattern = "yyyy-MM-dd HH:mm";
                    }else
                        pattern = "yyyy-MM-dd HH:mm:ss";
                else{
                    pattern = "yyyy-MM-dd";
                }
            } else if (str.contains("/")) {
                if (str.contains(" ") && str.contains(":"))
                    pattern = "yyyy/MM/dd HH:mm:ss";
                else
                    pattern = "yyyy/MM/dd";
            } else if(str.contains(":")&&str.split(":").length>1){
                pattern = "yyyy年MM月dd日 HH:mm:ss";
            }else {
                long milliseconds = Long.parseLong(str);
                return new Date(milliseconds * 1000);
            }
            format = new SimpleDateFormat(pattern);
            return format.parse(str);
        } catch (Exception e) {
            return new Date();
        }
    }
}
