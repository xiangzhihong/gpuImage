package com.xzh.gpuimage_master.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import com.xzh.gpuimage_master.model.PhotoItem;
import com.xzh.gpuimage_master.ui.CropActivity;

import java.util.Stack;

public class CameraManager {

    private static CameraManager mInstance;
    private Stack<Activity> cameras = new Stack<Activity>();

    public static CameraManager getInst() {
        if (mInstance == null) {
            synchronized (CameraManager.class) {
                if (mInstance == null)
                    mInstance = new CameraManager();
            }
        }
        return mInstance;
    }

    //打开照相界面
    public void openCamera(Context context, Bundle bundle) {
//        Intent intent = new Intent(context, CameraActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        context.startActivity(intent);
    }


    //判断图片是否需要裁剪
    public void processPhotoItem(Activity activity, PhotoItem photo, Bundle bundle) {
//        Uri uri = photo.imageUri.startsWith("file:") ? Uri.parse(photo
//                .imageUri) : Uri.parse("file://" + photo.imageUri);
//        Intent i = new Intent(activity, CropActivity.class);
//        if (bundle != null) {
//            i.putExtras(bundle);
//        }
//        i.setData(uri);
//        activity.startActivity(i);
    }

    //判断图片是否需要裁剪
    public void processCamera(Activity activity, PhotoItem photo, Bundle bundle) {
        Uri uri = photo.imageUri.startsWith("file:") ? Uri.parse(photo
                .imageUri) : Uri.parse("file://" + photo.imageUri);
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setData(uri);
        if (ImageUtils.isSquare(photo.imageUri)) {
//            intent.setClass(activity, ProcessPhotoActivity.class);
        } else {
            intent.setClass(activity, CropActivity.class);
        }
        activity.startActivity(intent);
    }


    public void close() {
        for (Activity act : cameras) {
            try {
                act.finish();
            } catch (Exception e) {

            }
        }
        cameras.clear();
    }

    public void addActivity(Activity act) {
        cameras.add(act);
    }

    public void removeActivity(Activity act) {
        cameras.remove(act);
    }


}
