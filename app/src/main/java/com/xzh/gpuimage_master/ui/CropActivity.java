package com.xzh.gpuimage_master.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagezoom.ImageViewTouch;
import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.base.BaseActivity;
import com.xzh.gpuimage_master.utils.DataHandler;
import com.xzh.gpuimage_master.utils.ImageUtils;
import com.xzh.gpuimage_master.utils.UIUtil;
import com.xzh.gpuimage_master.utils.ToastUtils;
import com.xzh.gpuimage_master.view.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 裁剪
 */
public class CropActivity extends BaseActivity {
    private Uri fileUri;
    private Bitmap oriBitmap;
    private int initWidth, initHeight;
    @Bind(R.id.crop_image)
    ImageViewTouch cropImage;
    @Bind(R.id.back)
    ImageView back;

    @Bind(R.id.tv_go_on)
    TextView tv_go_on;
    @Bind(R.id.pb_web_loading)
    ProgressWheel pb_web_loading;
    int screenW;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flagBarTint(false);
        setContentView(R.layout.activity_crop_layout);
        mContext = CropActivity.this;
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initParam();
        initView();
        initEvent();
    }

    private void initParam() {
        fileUri = getIntent().getData();
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_go_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage.setVisibility(View.VISIBLE);
                pb_web_loading.setVisibility(View.VISIBLE);
                CropTask task = new CropTask();//异步裁剪
                task.execute();

            }
        });
    }


    private void initView() {
        screenW = UIUtil.getScreenWidth(this);
        try {
            double rate = ImageUtils.getImageRadio(getContentResolver(), fileUri);
            oriBitmap = ImageUtils.decodeBitmapWithOrientation(fileUri.getPath(), screenW, UIUtil.getScreenHeight(this));
            initWidth = oriBitmap.getWidth();
            initHeight = oriBitmap.getHeight();
            cropImage.setImageBitmap(oriBitmap, new Matrix(), (float) rate, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String cropImage() {
        Bitmap croppedImage;
        try {
            croppedImage = decodeRegionCrop(cropImage);
        } catch (IllegalArgumentException e) {
            croppedImage = inMemoryCrop(cropImage);
        }
        return saveImageToCache(croppedImage);
    }

    private String saveImageToCache(Bitmap croppedImage) {
        if (croppedImage != null) {
            try {
                String imagePath = ImageUtils.saveToFile(getExternalCacheDir().getPath(), true,
                        croppedImage, 90);
                DataHandler.recycleBitmap(croppedImage);
                System.gc();
                return imagePath;
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.shortToast(mContext,"裁剪图片异常，请稍后重试");
                    }
                });
            }
        }
        return null;
    }

    @TargetApi(10)
    private Bitmap decodeRegionCrop(ImageViewTouch cropImage) {
        int width = Math.min(initHeight, initWidth);
        float scale = cropImage.getScale() / getImageRadio();
        RectF rectf = cropImage.getBitmapRect();
        int left = -(int) (rectf.left * width / screenW / scale);
        int top = -(int) (rectf.top * width / screenW / scale);
        int right = left + (int) (width / scale);
        int bottom = top + (int) (width / scale);
        Rect rect = new Rect(left, top, right, bottom);
        Bitmap croppedImage = Bitmap.createBitmap(oriBitmap, left, top, rect.width(), rect.height(), null, false);
        return croppedImage;
    }

    private float getImageRadio() {
        return Math.max((float) initWidth, (float) initHeight)
                / Math.min((float) initWidth, (float) initHeight);
    }

    private Bitmap inMemoryCrop(ImageViewTouch cropImage) {
        int width = initWidth > initHeight ? initHeight : initWidth;
        int screenWidth = screenW;
        System.gc();
        Bitmap croppedImage = null;
        try {
            croppedImage = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(croppedImage);
            float scale = cropImage.getScale();
            RectF srcRect = cropImage.getBitmapRect();
            Matrix matrix = new Matrix();
            matrix.postScale(scale / getImageRadio(), scale / getImageRadio());
            matrix.postTranslate(srcRect.left * width / screenWidth, srcRect.top * width
                    / screenWidth);
            canvas.drawBitmap(oriBitmap, matrix, null);
        } catch (OutOfMemoryError e) {
            System.gc();
            Log.e("OOM cropping image: " + e.getMessage(), e.toString());
        }

        return croppedImage;
    }

    @Override
    protected void onDestroy() {
        DataHandler.recycleBitmap(oriBitmap);
        super.onDestroy();
    }

    private class CropTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return cropImage();
        }

        @Override
        protected void onPostExecute(String result) {
            pb_web_loading.setVisibility(View.GONE);
            Intent intent = new Intent();
            if (getIntent().getExtras() != null) {
                intent.putExtras(getIntent().getExtras());
            }
            intent.setClass(CropActivity.this, ProcessPhotoActivity.class);
            String uriStr = "file://" + result;
            intent.setData(Uri.parse(uriStr));
            startActivity(intent);
            finish();
            super.onPostExecute(result);
        }
    }

}
