package com.xzh.gpuimage_master.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xzh.gpuimage_master.GPUImageApplication;

import java.util.List;

public abstract class ImageCreator {

    public ImageCreator() {
    }

    public void loadImage(String url) {
        ImageLoader.getInstance().loadImage(url, new ImageSize(800, 800), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                getDisplayImage(loadedImage);
                int needWidth = UIUtil.dp2px(80);
                new SmallPicTask().execute(ThumbnailUtils.extractThumbnail(loadedImage, needWidth, needWidth));
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                preload();
            }
        });
    }


    private class SmallPicTask extends AsyncTask<Bitmap, Void, List<Bitmap>> {

        @Override
        protected List<Bitmap> doInBackground(Bitmap[] params) {
            return DataHandler.getSmallPic(GPUImageApplication.getInstance(), params[0]);
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmapList) {
            getSmallImage(bitmapList);

        }
    }

    public abstract void preload();

    public abstract void getDisplayImage(Bitmap bitmap);

    public abstract void getSmallImage(List<Bitmap> smallList);

}
