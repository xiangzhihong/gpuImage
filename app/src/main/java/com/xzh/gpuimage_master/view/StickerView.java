package com.xzh.gpuimage_master.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.utils.UIUtil;


public class StickerView extends View {

    public float MAX_SCALE_SIZE = 3.0f;
    public float MIN_SCALE_SIZE = 0.3f;
    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;
    private int screenW;

    private float mLastPointX, mLastPointY;

    private Bitmap mBitmap;
    private Bitmap mControllerBitmap, mDeleteBitmap;
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private boolean mInController, mInMove;

    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;

    private OnStickerDeleteListener mOnStickerDeleteListener;
    private float mProgress;

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        screenW = UIUtil.getScreenWidth(getContext());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UIUtil.dp2px(1));
        mPaint.setColor(Color.WHITE);
        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#ff6c6c"));
        mBorderPaint.setShadowLayer(UIUtil.dp2px(2.0f), 0, 0, Color.parseColor("#33000000"));
        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aviary_resize_knob);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();
        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aviary_delete_knob);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

    }

    public void setWaterMark(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;
        setFocusable(true);
        try {
            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);
            mPoints = new float[10];
            mContentRect = new RectF();
            float maxLen = Math.max(px, py);
            float rate = ((screenW + 0.00f) / 3f) / maxLen;
            mMatrix = new Matrix();
            mMatrix.postScale(rate, rate);

            RectF rectF = new RectF();
            mMatrix.mapRect(rectF, mOriginContentRect);
            float translateLeft = ((float) UIUtil.getScreenWidth(getContext()) - rectF.right) / 2;
            float translateTop = ((float) UIUtil.getScreenWidth(getContext()) - rectF.bottom) / 2;
            mMatrix.postTranslate(translateLeft, translateTop);

        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidate();

    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }

        mMatrix.mapPoints(mPoints, mOriginPoints);

        mMatrix.mapRect(mContentRect, mOriginContentRect);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        if (mDrawController && isFocusable()) {
            canvas.drawLine(mPoints[0], mPoints[1], mPoints[0] + (mPoints[2] - mPoints[0]) * mProgress, mPoints[1] + (mPoints[3] - mPoints[1]) * mProgress, mBorderPaint);
            canvas.drawLine(mPoints[4], mPoints[5], (mPoints[2] - mPoints[4]) * mProgress + mPoints[4], (mPoints[3] - mPoints[5]) * mProgress + mPoints[5], mBorderPaint);
            canvas.drawLine(mPoints[2], mPoints[3], (mPoints[4] - mPoints[2]) * mProgress + mPoints[2], (mPoints[5] - mPoints[3]) * mProgress + mPoints[3], mBorderPaint);
            canvas.drawLine(mPoints[4], mPoints[5], (mPoints[6] - mPoints[4]) * mProgress + mPoints[4], (mPoints[7] - mPoints[5]) * mProgress + mPoints[5], mBorderPaint);
            canvas.drawLine(mPoints[0], mPoints[1], (mPoints[6] - mPoints[0]) * mProgress + mPoints[0], (mPoints[7] - mPoints[1]) * mProgress + mPoints[1], mBorderPaint);
            // canvas.drawLine(mPoints[6], mPoints[7], (mPoints[0] - mPoints[6]) * mProgress + mPoints[6], (mPoints[1] - mPoints[7]) * mProgress + mPoints[7], mBorderPaint);
            canvas.drawBitmap(mControllerBitmap, mPoints[4] - mControllerWidth / 2, mPoints[5] - mControllerHeight / 2, mBorderPaint);
            canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawController = false;
        draw(canvas);
        mDrawController = true;
        canvas.save();
        return bitmap;
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
    }


    public boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2,
                ry - mControllerHeight / 2,
                rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

    }

    public boolean isInDelete(float x, float y) {
        int position = 0;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

    }


    private boolean mInDelete = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusable()) {
            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInController(x, y)) {
                    mInController = true;
                    mLastPointY = y;
                    mLastPointX = x;
                    break;
                }

                if (isInDelete(x, y)) {
                    mInDelete = true;
                    break;
                }

                if (mContentRect.contains(x, y)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInDelete(x, y) && mInDelete) {
                    doDeleteSticker();
                }
            case MotionEvent.ACTION_CANCEL:
                mLastPointX = 0;
                mLastPointY = 0;
                mInController = false;
                mInMove = false;
                mInDelete = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInController) {
                    mMatrix.postRotate(rotation(event), mPoints[8], mPoints[9]);
                    float nowLenght = calculateLength(mPoints[0], mPoints[1]);
                    float touchLenght = calculateLength(event.getX(), event.getY());
                    if (Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                        float scale = touchLenght / nowLenght;
                        float nowsc = mStickerScaleSize * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            mMatrix.postScale(scale, scale, mPoints[8], mPoints[9]);
                            mStickerScaleSize = nowsc;
                        }
                    }
                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;
                }

                if (mInMove == true) { //拖动的操作
                    float cX = x - mLastPointX;
                    float cY = y - mLastPointY;
                    mInController = false;
                    if (Math.sqrt(cX * cX + cY * cY) > 2.0f && canStickerMove(cX, cY)) {
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                    }
                    break;
                }
                return true;
        }
        return true;
    }

    private void doDeleteSticker() {//可根据需求添加 3种常规布局
        if (getParent() != null) {
            if (getParent() instanceof FrameLayout) {
                ((FrameLayout) getParent()).removeView(this);
            } else if (getParent() instanceof RelativeLayout) {
                ((RelativeLayout) getParent()).removeView(this);
            } else if (getParent() instanceof LinearLayout) {
                ((LinearLayout) getParent()).removeView(this);
            } else {
                throw (new IllegalArgumentException("parent must instanceof FrameLayout ,LinearLayout or RelativeLayout"));
            }
        }
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete(this);
        }
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        return mViewRect.contains(px, py);
    }


    private float calculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return (float) Math.sqrt(ex * ex + ey * ey);
    }


    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mPoints[8];
        double delta_y = y - mPoints[9];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public RectF getContentRect() {
        return mContentRect;
    }

    public void addTo(ViewGroup overlay) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenW, screenW);
        overlay.addView(this, params);
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "percentage", 0.0f, 1.0f);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    public interface OnStickerDeleteListener {
        void onDelete(StickerView stickerView);
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }

    public void setPercentage(float percentage) {
        if (percentage < 0.0f || percentage > 1.0f)
            throw new IllegalArgumentException("setPercentage not between 0.0f and 1.0f");
        mProgress = percentage;
        invalidate();
    }

}
