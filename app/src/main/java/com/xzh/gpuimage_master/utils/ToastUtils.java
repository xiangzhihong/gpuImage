package com.xzh.gpuimage_master.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.xzh.gpuimage_master.GPUImageApplication;

public class ToastUtils {
    public static void shortToast(Context context, int resId) {
        toast(context, context.getString(resId), Toast.LENGTH_SHORT);
    }

    public static void shortToast(Context context, CharSequence text) {
        toast(context, text, Toast.LENGTH_SHORT);
    }

    public static void shortToast(CharSequence text) {
        toast(GPUImageApplication.getInstance(), text, Toast.LENGTH_SHORT);
    }


    public static void longToast(Context context, CharSequence text) {
        toast(context, text, Toast.LENGTH_LONG);
    }

    public static void toast(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        TextView v = new TextView(context);
        v.setBackgroundColor(0x88000000);
        v.setTextColor(Color.WHITE);
        v.setText(text);
        v.setSingleLine(false);
        v.setPadding(20, 10, 20, 10);
        v.setGravity(Gravity.CENTER);
        toast.setView(v);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
