package com.xzh.gpuimage_master.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.model.EventType;
import com.xzh.gpuimage_master.utils.ImageUtils;

import de.greenrobot.event.EventBus;

public class BottomTabView extends LinearLayout {

    private TextView tv_filter_right;
    private TextView tv_filter_left;
    private TextView tv_filter_mid;

    public BottomTabView(Context context) {
        this(context, null);
    }

    public BottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setWeightSum(3);
        init();
    }


    private void init() {
        View viewLeft = LayoutInflater.from(getContext()).inflate(R.layout.tab_view_layout, null);
        initLeft(viewLeft);
        View viewMid = LayoutInflater.from(getContext()).inflate(R.layout.tab_view_layout, null);
        initMid(viewMid);
        View viewRight = LayoutInflater.from(getContext()).inflate(R.layout.tab_view_layout, null);
        initRight(viewRight);
        setLeftStatus(true);
        setMidStatus(false);
        setRightStatus(false);
    }

    private void setMidStatus(boolean b) {
        if (b) {
            tv_filter_mid.setTextColor(getResources().getColor(R.color.c12));
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.icon_mark_selected);
            ImageUtils.setDrawableLeft(tv_filter_mid,drawable);
        } else {
            tv_filter_mid.setTextColor(getResources().getColor(R.color.c10));
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.icon_mark_normal);
            ImageUtils.setDrawableLeft(tv_filter_mid,drawable);
        }
    }

    private void initMid(View viewMid) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        addView(viewMid, params);
        viewMid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftStatus(false);
                setRightStatus(false);
                setMidStatus(true);
                EventBus.getDefault().post(new EventType(-2));
            }
        });
        tv_filter_mid = (TextView) viewMid.findViewById(R.id.tv_filter);
        tv_filter_mid.setText("贴纸");
    }

    private void initLeft(View viewLeft) {

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        addView(viewLeft, params);
        viewLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftStatus(true);
                setRightStatus(false);
                setMidStatus(false);
                EventBus.getDefault().post(new EventType(-1));
            }
        });
        tv_filter_left = (TextView) viewLeft.findViewById(R.id.tv_filter);

    }

    private void setLeftStatus(boolean b) {
        if (b) {
            tv_filter_left.setTextColor(getResources().getColor(R.color.c12));
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.icon_filter_selected);
            ImageUtils.setDrawableLeft(tv_filter_left,drawable);
        } else {
            tv_filter_left.setTextColor(getResources().getColor(R.color.c10));
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.icon_filter_normal);
            ImageUtils.setDrawableLeft(tv_filter_left,drawable);
        }
    }

    private void initRight(View viewRight) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        addView(viewRight, params);
        viewRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftStatus(false);
                setRightStatus(true);
                setMidStatus(false);
                EventBus.getDefault().post(new EventType(-3));
            }
        });
        tv_filter_right = (TextView) viewRight.findViewById(R.id.tv_filter);
        tv_filter_right.setText("标签");
        setRightStatus(false);
    }

    private void setRightStatus(boolean b) {
        if (b) {
            tv_filter_right.setTextColor(getResources().getColor(R.color.c12));
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.icon_tag_selected);
            ImageUtils.setDrawableLeft(tv_filter_right,drawable);
        } else {
            tv_filter_right.setTextColor(getResources().getColor(R.color.c10));
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.icon_tag_normal);
            ImageUtils.setDrawableLeft(tv_filter_right,drawable);
        }
    }

}
