package com.xzh.gpuimage_master.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtil {
    protected Activity mActivity;

    public static ActivityUtil createInstance(Activity activity) {
        return new ActivityUtil(activity);
    }

    protected ActivityUtil(Activity activity) {
        mActivity = activity;
    }

    public static void startActivity(Context context, Class<? extends Activity> toklass) {
        Intent intent = new Intent(context, toklass);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> toklass, Bundle extras) {
        Intent intent = new Intent(context, toklass);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity fromActivity, Class<? extends Activity> toklass,
                                              int requestCode) {
        Intent intent = new Intent(fromActivity, toklass);
        fromActivity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity fromActivity, Class<? extends Activity> toklass,
                                              int requestCode, Bundle extras) {
        Intent intent = new Intent(fromActivity, toklass);
        intent.putExtras(extras);
        fromActivity.startActivityForResult(intent, requestCode);
    }
}
