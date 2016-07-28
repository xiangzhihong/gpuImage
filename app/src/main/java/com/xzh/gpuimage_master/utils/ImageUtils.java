package com.xzh.gpuimage_master.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;

import com.xzh.gpuimage_master.GPUImageApplication;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 图片工具类
 */
public class ImageUtils {

    public static int getMiniSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return Math.min(options.outHeight, options.outWidth);
    }

    public static boolean isSquare(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return options.outHeight == options.outWidth;
    }

    public static String getImagePath() {
        String pictruePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        String fileName = "";
        File file = new File(pictruePath);
        if (!file.exists())
            file.mkdirs();
        fileName = pictruePath + getFileName();
        return fileName;
    }

    public static String getFileName() {
        String fileName = System.currentTimeMillis() + ".jpg";
        return fileName;
    }
    public static String getBasePath() {
        String BASE_PATH;
        String sdcardState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
            BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            BASE_PATH = GPUImageApplication.getInstance().getCacheDir().getAbsolutePath();
        }
        return BASE_PATH;
    }

    public static String saveCropPath() {
        String fileDir = getBasePath() + "/DCIM/洋码头/";
        File file = new File(fileDir);
        if (!file.exists()) {
            mkdir(new File(fileDir));
        }
        return fileDir;
    }

    public static void setDrawableLeft(TextView textView, Drawable drawable) {
        Drawable mdrawable = drawable;
        mdrawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    //图片是不是正方形
    public static boolean isSquare(Uri imageUri) {
        ContentResolver resolver = GPUImageApplication.getInstance().getContentResolver();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(resolver.openInputStream(imageUri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return options.outHeight == options.outWidth;
    }

    private static Bitmap decodeBitmapWithSize(String pathName, int width, int height,
                                               boolean useBigger) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inInputShareable = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(pathName, options);
        int decodeWidth = width, decodeHeight = height;
        final int degrees = getImageDegrees(pathName);
        if (degrees == 90 || degrees == 270) {
            decodeWidth = height;
            decodeHeight = width;
        }

        if (useBigger) {
            options.inSampleSize = (int) Math.min(((float) options.outWidth / decodeWidth),
                    ((float) options.outHeight / decodeHeight));
        } else {
            options.inSampleSize = (int) Math.max(((float) options.outWidth / decodeWidth),
                    ((float) options.outHeight / decodeHeight));
        }
        options.inJustDecodeBounds = false;
        Bitmap sourceBm = BitmapFactory.decodeFile(pathName, options);
        return imageWithFixedRotation(sourceBm, degrees);
    }

    public static int getImageDegrees(String pathName) {
        int degrees = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(pathName);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degrees;
    }

    public static Bitmap imageWithFixedRotation(Bitmap bm, int degrees) {
        if (bm == null || bm.isRecycled())
            return null;

        if (degrees == 0)
            return bm;

        final Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (result != bm)
            bm.recycle();
        return result;

    }


    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


    //异步加载图片
    public interface LoadImageCallback {
        void callback(Bitmap result);
    }

    public static void asyncLoadImage(Context context, Uri imageUri, LoadImageCallback callback) {
        new LoadImageUriTask(context, imageUri, callback).execute();
    }

    private static class LoadImageUriTask extends AsyncTask<Void, Void, Bitmap> {
        private final Uri imageUri;
        private final Context context;
        private LoadImageCallback callback;

        public LoadImageUriTask(Context context, Uri imageUri, LoadImageCallback callback) {
            this.imageUri = imageUri;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            InputStream inputStream = null;
            try {
                if (imageUri.getScheme().startsWith("http")
                        || imageUri.getScheme().startsWith("https")) {
                    inputStream = new URL(imageUri.toString()).openStream();
                } else {
                    inputStream = context.getContentResolver().openInputStream(imageUri);
                }
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            callback.callback(result);
        }
    }


    public static float getImageRadio(ContentResolver resolver, Uri fileUri) {
        InputStream inputStream = null;
        try {
            inputStream = resolver.openInputStream(fileUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            int initWidth = options.outWidth;
            int initHeight = options.outHeight;
            float rate = initHeight > initWidth ? (float) initHeight / (float) initWidth
                    : (float) initWidth / (float) initHeight;
            return rate;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static Bitmap decodeBitmapWithOrientation(String pathName, int width, int height) {
        return decodeBitmapWithSize(pathName, width, height, false);
    }

    public static String saveToFile(String fileFolderStr, boolean isDir, Bitmap croppedImage, int quality) throws IOException {
        final File jpgFile;
        File fileFolder = null;
        if (isDir) {
            fileFolder = new File(fileFolderStr);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            String filename = format.format(date) + ".jpg";
            if (!fileFolder.exists()) {
                mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, filename);
        } else {
            jpgFile = new File(fileFolderStr);
            if (!jpgFile.getParentFile().exists()) {
                mkdir(jpgFile.getParentFile());
            }
        }

        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        croppedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        MediaScannerConnection.scanFile(
                GPUImageApplication.getInstance(),
                new String[]{jpgFile.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                }
        );
        DiaryUtils.closeStream(outputStream);
        return jpgFile.getPath();
    }

    public static void mkdir(File dir) {
        try {
            if (dir == null) return;
            if (!dir.exists()) {
                mkdir(dir.getParentFile());
                dir.mkdir();
            }
        } catch (Exception e) {
        }
    }

}
