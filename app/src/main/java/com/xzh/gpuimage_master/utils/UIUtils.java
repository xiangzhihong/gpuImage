package com.xzh.gpuimage_master.utils;

import android.view.View;

public class UIUtils {
    public static <T extends View> T getView(View rootView, int id) {
        return (T) rootView.findViewById(id);
    }

    public static <T extends Object> T getT(Object obj) {
        return (T) obj;
    }
}
