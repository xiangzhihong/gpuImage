package com.xzh.gpuimage_master.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.model.PathObj;
import com.xzh.gpuimage_master.model.Tag;
import com.xzh.gpuimage_master.model.TagInfo;
import com.xzh.gpuimage_master.ui.EditTagActivity;
import com.xzh.gpuimage_master.ui.ProcessPhotoActivity;
import com.xzh.gpuimage_master.utils.Constants;
import com.xzh.gpuimage_master.utils.DataHandler;
import com.xzh.gpuimage_master.utils.UIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagView extends ViewGroup {
    private TextView[] tags = new TextView[8];
    private Paint paint;
    private List<Point> points = new ArrayList<>();
    private int centerX;
    private int centerY;
    private int dis;
    private RectF localRectF1;
    private RectF localRectF2;
    private RectF dragRectF;
    private RectF touchRange;
    private int lineStroke;
    int type = 0;
    float pointX;
    float pointY;
    private TagInfo originalInfo;
    private int innerR;
    private int outerR;
    private TextView[] tvs;
    private int i = 0;
    private List<Rect> rectList = new ArrayList<>();
    private boolean isMove = false;
    RectF rectFAll;
    private int screenW;

    private List<Tag> tagList = new ArrayList<>();
    private boolean isJump = true;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenW = UIUtil.getScreenWidth(getContext());
        innerR = screenW / 60;
        outerR = screenW / 40;
        dis = screenW / 15;
        lineStroke = UIUtil.dp2px(1);
        rectFAll = new RectF(0, 0, screenW, screenW);

        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setColor(-1);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setTextSize(12.0F);
        this.paint.setStrokeWidth(1.0F);
        for (int i = 0; i < 8; i++) {
            MarginLayoutParams localMarginLayoutParams = new MarginLayoutParams(-2, -2);
            this.tags[i] = new TextView(getContext());
            this.tags[i].setTextSize(12.0F);
            this.tags[i].setShadowLayer(1.6F * getResources().getDisplayMetrics().density, 0.0F, 0.0F, Color.argb(204, 0, 0, 0));
            this.tags[i].setTextColor(-1);
            this.tags[i].setSingleLine();
            this.tags[i].setMaxWidth(screenW / 2 - dis-20);
            this.tags[i].setPadding(12, 4, 12, 4);
            this.tags[i].setCompoundDrawablePadding(4);
            this.tags[i].setTag(Integer.valueOf(i));
            this.tags[i].setVisibility(INVISIBLE);
            addView(this.tags[i], localMarginLayoutParams);
        }

    }


    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        ArrayList<PathObj> pathObj = new ArrayList<PathObj>();
        localRectF1 = new RectF(centerX - innerR, centerY - innerR, centerX + innerR, centerY + innerR);
        localRectF2 = new RectF(centerX - outerR, centerY - outerR, centerX + outerR, centerY + outerR);
        dragRectF = new RectF(centerX - dis, centerY - dis, centerX + dis, centerY + dis);
        touchRange = new RectF(centerX - outerR - 20, centerY - 20 - outerR, centerX + outerR + 20, centerY + outerR + 20);
        this.paint.setColor(Color.parseColor("#30000000"));
        canvas.drawOval(localRectF2, this.paint);
        this.paint.setAlpha(255);
        this.paint.setColor(Color.WHITE);
        canvas.drawOval(localRectF1, this.paint);
        float line1[] = {centerX, centerY, centerX + dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};
        float line2[] = {centerX, centerY, centerX + dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};
        float line3[] = {centerX, centerY, centerX + dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};
        float line4[] = {centerX, centerY, centerX + dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};

        View view = getChildAt(0);
        measureChild(view, -2, -2);
        int w = view.getMeasuredWidth();
        float line5[] = {centerX + dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)), centerX + dis + w,
                centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};

        view = getChildAt(1);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line6[] = {centerX + dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)), centerX + dis + w,
                centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};

        view = getChildAt(2);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line7[] = {centerX + dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)), centerX + dis + w,
                centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};

        view = getChildAt(3);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line8[] = {centerX + dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)), centerX + dis + w,
                centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};

        float line11[] = {centerX, centerY, centerX - dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};
        float line22[] = {centerX, centerY, centerX - dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};
        float line33[] = {centerX, centerY, centerX - dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};
        float line44[] = {centerX, centerY, centerX - dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};

        view = getChildAt(4);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line55[] = {centerX - dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)), centerX - dis - w,
                centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};


        view = getChildAt(5);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line66[] = {centerX - dis, centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)), centerX - dis - w,
                centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};


        view = getChildAt(6);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line77[] = {centerX - dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)), centerX - dis - w,
                centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER))};
        view = getChildAt(7);
        measureChild(view, -2, -2);
        w = view.getMeasuredWidth();
        float line88[] = {centerX - dis, centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)), centerX - dis - w,
                centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2))};

        float[][] objArr = {line1, line2, line3, line4};
        float[][] objArr2 = {line5, line6, line7, line8};
        float[][] objArr3 = {line11, line22, line33, line44};
        float[][] objArr4 = {line55, line66, line77, line88};
        for (int i = 0; i < 4; i++) {
            pathObj.add(createPathObj(objArr[i], objArr2[i], tags[i]));
        }
        for (int i = 0; i < 4; i++) {
            pathObj.add(createPathObj(objArr3[i], objArr4[i], tags[i + 4]));
        }
        ArrayList<PathObj> temp;

        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineStroke);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (type == 12) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(2));
            temp.add(pathObj.get(5));
            temp.add(pathObj.get(6));
            temp.add(pathObj.get(7));

            tvs = new TextView[]{((TextView) getChildAt(2)), ((TextView) getChildAt(5)), (TextView) getChildAt(6), (TextView) getChildAt(7)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        } else if (type == 11) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(1));
            temp.add(pathObj.get(2));
            temp.add(pathObj.get(3));
            temp.add(pathObj.get(6));
            tvs = new TextView[]{((TextView) getChildAt(1)), ((TextView) getChildAt(2)), (TextView) getChildAt(3), (TextView) getChildAt(6)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }

            drawLineAndText(temp, canvas);
        } else if (type == 10) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(1));
            temp.add(pathObj.get(2));
            temp.add(pathObj.get(5));
            temp.add(pathObj.get(6));
            tvs = new TextView[]{((TextView) getChildAt(1)), ((TextView) getChildAt(2)), (TextView) getChildAt(5), (TextView) getChildAt(6)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        } else if (type == 9) {
            temp = new ArrayList<>();
            temp.addAll(pathObj.subList(0, 4));
            tvs = new TextView[]{((TextView) getChildAt(0)), ((TextView) getChildAt(1)), (TextView) getChildAt(2), (TextView) getChildAt(3)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        } else if (type == 8) {
            temp = new ArrayList<>();
            temp.addAll(pathObj.subList(4, 8));
            tvs = new TextView[]{((TextView) getChildAt(4)), ((TextView) getChildAt(5)), (TextView) getChildAt(6), (TextView) getChildAt(7)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }

            drawLineAndText(temp, canvas);

        } else if (type == 7) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(2));
            temp.add(pathObj.get(5));
            temp.add(pathObj.get(6));
            tvs = new TextView[]{((TextView) getChildAt(2)), ((TextView) getChildAt(5)), (TextView) getChildAt(6)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        } else if (type == 6) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(1));
            temp.add(pathObj.get(2));
            temp.add(pathObj.get(6));
            tvs = new TextView[]{((TextView) getChildAt(1)), ((TextView) getChildAt(2)), (TextView) getChildAt(6)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);

        } else if (type == 5) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(1));
            temp.add(pathObj.get(2));
            temp.add(pathObj.get(3));
            tvs = new TextView[]{((TextView) getChildAt(1)), ((TextView) getChildAt(2)), (TextView) getChildAt(3)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);

        } else if (type == 4) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(5));
            temp.add(pathObj.get(6));
            temp.add(pathObj.get(7));
            tvs = new TextView[]{((TextView) getChildAt(5)), ((TextView) getChildAt(6)), (TextView) getChildAt(7)};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);

        } else if (type == 3) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(1));
            temp.add(pathObj.get(2));
            tvs = new TextView[]{((TextView) getChildAt(1)), ((TextView) getChildAt(2))};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);

        } else if (type == 2) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(5));
            temp.add(pathObj.get(6));
            tvs = new TextView[]{((TextView) getChildAt(5)), ((TextView) getChildAt(6))};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        } else if (type == 1) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(2));
            tvs = new TextView[]{((TextView) getChildAt(2))};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        } else if (type == 0) {
            temp = new ArrayList<>();
            temp.add(pathObj.get(6));
            tvs = new TextView[]{((TextView) getChildAt(6))};
            for (int i = 0; i < tagList.size(); i++) {
                tvs[i].setText(tagList.get(i).TagVal);
            }
            drawLineAndText(temp, canvas);
        }

    }


    private void drawLineAndText(ArrayList<PathObj> pathObj, Canvas canvas) {
        if (pathObj != null && canvas != null) {
            for (PathObj obj : pathObj) {
                canvas.drawLines(obj.line1, paint);
                canvas.drawLines(obj.line2, paint);
                obj.tv.setVisibility(VISIBLE);
            }
        }
    }

    private PathObj createPathObj(float[] line1, float[] line2, TextView tag) {
        PathObj obj = new PathObj();
        obj.line1 = line1;
        obj.line2 = line2;
        obj.tv = tag;
        return obj;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        initPoint();
        rectList.clear();
        for (int i = 0; i < childCount; i++) {
            View viewLabel = getChildAt(i);
            measureChild(viewLabel, -2, -2);
            int h = viewLabel.getMeasuredHeight();
            int w = viewLabel.getMeasuredWidth();
            Point point = points.get(i);
            if (i < 4) {
                Rect rect = new Rect(point.x, point.y - h, point.x + w, point.y);
                rectList.add(rect);
                viewLabel.layout(rect.left, rect.top, rect.right, rect.bottom);
            } else if (i >= 4 && i < 8) {
                Rect rect = new Rect(point.x - w, point.y - h, point.x, point.y);
                rectList.add(rect);
                viewLabel.layout(rect.left, rect.top, rect.right, rect.bottom);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (!isMove) {
            initCenter();
        }
        setMeasuredDimension(width, height);
    }

    private void initCenter() {
        centerX = Math.round(pointX * screenW);
        centerY = Math.round(pointY * screenW);
        int offset = (int) (dis * Math.tan((DataHandler.DIFF_DEGREE + DataHandler.DIFF_DEGREE_INNER) * DataHandler.UNIT) + UIUtil.sp2px(14) + 10);
        if (centerY < offset) {
            centerY = offset;
        } else if (screenW - centerY < offset) {
            centerY = screenW - offset;
        }
        if (centerX < screenW / 30) {
            centerX = screenW / 30;
        }
        if (screenW - centerX < screenW / 30) {
            centerX = screenW - screenW / 30;
        }
    }

    private void initPoint() {
        points.clear();
        points.add(new Point(centerX + dis, Math.round(centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)))));
        points.add(new Point(centerX + dis, Math.round(centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)))));
        points.add(new Point(centerX + dis, Math.round(centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)))));
        points.add(new Point(centerX + dis, Math.round(centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)))));
        points.add(new Point(centerX - dis, Math.round(centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)))));
        points.add(new Point(centerX - dis, Math.round(centerY - Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)))));
        points.add(new Point(centerX - dis, Math.round(centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE_INNER)))));
        points.add(new Point(centerX - dis, Math.round(centerY + Math.round(dis * Math.tan(DataHandler.UNIT * DataHandler.DIFF_DEGREE * 2)))));
        points.add(new Point(centerX, centerY));
    }

    int startX;
    int startY;
    int pointDownX = 0;
    int pointDownY = 0;
    boolean isCenter = false;
    boolean isInTag = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int disY = 0;
        int disX = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isInTag = false;
                isCenter = touchRange.contains(event.getX(), event.getY());
                for (Rect rect : rectList) {
                    if (rect.contains((int) event.getX(), (int) event.getY())) {
                        isInTag = true;
                        break;
                    }
                }
                startX = (int) event.getX();
                startY = (int) event.getY();
                pointDownX = (int) event.getX();
                pointDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                disX = (int) event.getX() - startX;
                disY = (int) event.getY() - startY;
                startX = (int) event.getX();
                startY = (int) event.getY();
                isMove = true;
                updateLocation(disX, disY);
                break;

            case MotionEvent.ACTION_UP:
                if (Math.max(Math.abs(event.getY() - pointDownY), Math.abs(event.getX() - pointDownX)) < 5) {
                    if (localRectF2.contains(event.getX(), event.getY())) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            switchStyle();
                            return true;
                        }
                    }
                    if (isInTag) {
                        skipEvent();
                        return true;
                    }
                }
                break;
        }

        return isCenter || isInTag;
    }

    private void skipEvent() {
        if (isJump) {
            Intent intent = new Intent(getContext(), EditTagActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(Constants.EXTRA_DATA, getTagInfo());
            intent.putExtra(Constants.IS_EDIT, true);
            getContext().startActivity(intent);
            ((ProcessPhotoActivity) getContext()).overridePendingTransition(R.anim.push_bottom_in, 0);
        }
    }

    private void switchStyle() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!checkMargin()) {
                    List<Integer> list = new ArrayList<Integer>();
                    int offset = (int) (dis * Math.tan((DataHandler.DIFF_DEGREE + DataHandler.DIFF_DEGREE_INNER) * DataHandler.UNIT) + UIUtil.sp2px(14) + 10);
                    for (TextView textView : tvs) {
                        list.add(textView.getWidth());
                    }
                    int max = 0;
                    if (list != null && list.size() > 0) {
                        max = Collections.max(list);
                    }
                    if (centerX > screenW / 2) {
                        centerX = centerX - max - dis - 10;
                        centerX = centerX < screenW / 2 ? screenW / 2 : centerX;

                    } else {
                        centerX = centerX + max + dis + 10;
                        centerX = centerX > screenW / 2 ? screenW / 2 : centerX;
                    }
                    if (centerY < offset) {
                        centerY = offset;
                    } else if (screenW - centerY < offset) {
                        centerY = screenW - offset;
                    }
                    isMove = true;
                    invalidate();
                    requestLayout();
                }
            }
        }, 500);
        i++;
        if (type >= 8 && type <= 12) {
            if (i % 5 == 0) {
                type = 9;
            } else if (i % 5 == 1) {
                type = 8;
            } else if (i % 5 == 2) {
                type = 10;
            } else if (i % 5 == 3) {
                type = 11;
            } else if (i % 5 == 4) {
                type = 12;
            }
        }
        if (type >= 4 && type <= 7) {
            if (i % 4 == 0) {
                type = 5;
            } else if (i % 4 == 1) {
                type = 4;
            } else if (i % 4 == 2) {
                type = 6;
            } else if (i % 4 == 3) {
                type = 7;
            }
        }
        if (type == 2 || type == 3) {
            if (i % 2 == 0) {
                type = 2;
            } else if (i % 2 == 1) {
                type = 3;
            }
        }
        if (type == 0 || type == 1) {
            if (i % 2 == 0) {
                type = 1;
            } else if (i % 2 == 1) {
                type = 0;
            }
        }

        //type=8;

        originalInfo.ImageTagStyle = type;
        for (int i = 0; i < 8; i++) {
            tags[i].setVisibility(INVISIBLE);
        }
        invalidate();
    }


    public void setTagInfo(TagInfo info, boolean isEdit) {
        this.originalInfo = info;
        if (!isEdit) {
            if (info != null) {
                switch (info.Tags.size()) {
                    case 1:
                        if (originalInfo.XPoint * screenW > screenW / 2) {
                            originalInfo.ImageTagStyle = 0;
                        } else {
                            originalInfo.ImageTagStyle = 1;
                        }

                        break;
                    case 2:
                        if (originalInfo.XPoint * screenW > screenW / 2) {
                            originalInfo.ImageTagStyle = 2;
                        } else {
                            originalInfo.ImageTagStyle = 3;
                        }
                        break;
                    case 3:
                        if (originalInfo.XPoint * screenW > screenW / 2) {
                            originalInfo.ImageTagStyle = 4;
                        } else {
                            originalInfo.ImageTagStyle = 5;
                        }
                        break;
                    case 4:
                        if (originalInfo.XPoint * screenW > screenW / 2) {
                            originalInfo.ImageTagStyle = 8;
                        } else {
                            originalInfo.ImageTagStyle = 9;
                        }
                        break;
                }
            }

        }
        setCenterPoint(originalInfo.XPoint, originalInfo.YPoint);
        tagList = originalInfo.Tags;
        type = originalInfo.ImageTagStyle;
        invalidate();
    }

    public TagInfo getTagInfo() {
        originalInfo.XPoint = (centerX + 0.00F) / (UIUtil.getScreenWidth(getContext()) + 0.00F);
        originalInfo.YPoint = (centerY + 0.00F) / (UIUtil.getScreenWidth(getContext()) + 0.00F);
        return originalInfo;
    }

    public void setCenterPoint(float pointX, float pointY) {
        this.pointY = pointY;
        this.pointX = pointX;
    }

    int lastDisX;
    int lastDisY;

    public void updateLocation(int disX, int disY) {

        if (checkMargin()) {
            centerX += disX;
            centerY += disY;
            lastDisX = disX;
            lastDisY = disY;
        } else {
            centerY -= lastDisY;
            centerX -= lastDisX;
        }
        invalidate();
        requestLayout();
    }

    public Point getCenterPoint() {
        Point point = new Point();
        point.x = centerX;
        point.y = centerY;
        return point;
    }

    private boolean checkMargin() {
        if (!rectFAll.contains(localRectF2)) {
            return false;
        }
        for (TextView textView : tvs) {
            if (!rectFAll.contains(textView.getLeft(), textView.getTop(), textView.getRight(), textView.getBottom())) {
                return false;
            }
        }
        return true;
    }

    public void setJump(boolean isJump) {
        this.isJump = isJump;
    }

}
