package com.xzh.gpuimage_master.utils.imageloader;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xzh.gpuimage_master.GPUImageApplication;
import com.xzh.gpuimage_master.R;

public class GPUImageLoader {
    private final static int limitMinMemoryCacheSize = 8 * ByteConstants.MB;

    public static void build() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(GPUImageApplication.getInstance())
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCache(new LruMemoryCache(getMemoryCacheSize()))
                .diskCacheSize(50 * ByteConstants.MB)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(200)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(getSimpleOptions().build())
                .build();
        ImageLoader.getInstance().init(config);
    }

    private static DisplayImageOptions.Builder getSimpleOptions() {
        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnLoading(R.drawable.loader_default_icon)
                .showImageForEmptyUri(R.drawable.loader_default_icon)
                .showImageOnFail(R.drawable.loader_default_icon)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888);
        return options;
    }

    private static int getMemoryCacheSize() {
        int memoryCacheSize = (int) (Runtime.getRuntime().freeMemory() / 8);
        if (memoryCacheSize < limitMinMemoryCacheSize) {
            memoryCacheSize = limitMinMemoryCacheSize;
        }
        return memoryCacheSize;
    }

    public static void clearDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }

    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static void loaderRoundImage(String url, ImageView imageView) {
        displayImage(url, imageView, new RoundedBitmapDisplayer(5));
    }

    public static void loaderRoundImage(String url, ImageView imageView, int cornerRadiusPixels) {
        if (TextUtils.isEmpty(url)) {
            displayImage("", imageView, new RoundedBitmapDisplayer(cornerRadiusPixels));
            return;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            displayImage(url, imageView, new RoundedBitmapDisplayer(cornerRadiusPixels));
        } else {
            displayImage("file://" + url, imageView, new RoundedBitmapDisplayer(cornerRadiusPixels));
        }
    }

    public static void imageloader(String url, ImageView imageView) {
        displayImage(url, imageView, null);
    }

    public static void displayGalleryImage(String url, ImageView imageView) {
        DisplayImageOptions.Builder builder = getSimpleOptions();
        builder.cacheOnDisk(false)
                .showImageOnLoading(0)
                .showImageForEmptyUri(0)
                .showImageOnFail(0);
        imageloader(url, imageView, builder.build());
    }

    public static void displayImage(String url, ImageView imageView) {
        DisplayImageOptions.Builder builder = null;
        imageloader(url, imageView);
    }

    public static void displayImage(String url, ImageView imageView, RoundedBitmapDisplayer roundedBitmapDisplayer) {
        DisplayImageOptions.Builder builder = null;
        if (roundedBitmapDisplayer != null) {
            builder = getSimpleOptions();
            builder.displayer(roundedBitmapDisplayer);
        }
        imageloader(url, imageView, builder == null ? null : builder.build());
    }

    public static void imageloader(String url, ImageView imageView, int holdResId, SimpleImageLoadingListener listener) {
        if (imageView == null) return;
        DisplayImageOptions.Builder builder = getSimpleOptions();
        builder.showImageOnLoading(holdResId)
                .showImageForEmptyUri(holdResId)
                .showImageOnFail(holdResId);
        ImageLoader.getInstance().displayImage(url, imageView, builder.build(), listener);
    }

    public static void imageloader(String url, ImageView imageView, SimpleImageLoadingListener listener) {
        if (imageView == null) return;
        ImageLoader.getInstance().displayImage(url, imageView, listener);
    }

    public static void loadImage(String url, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(url, listener);
    }

    public static void imageloader(String url, ImageView imageView, DisplayImageOptions options) {
        if (imageView == null) return;
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }
}
