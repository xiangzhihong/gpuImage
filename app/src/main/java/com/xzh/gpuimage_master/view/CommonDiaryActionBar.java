package com.xzh.gpuimage_master.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.xzh.gpuimage_master.R;

import butterknife.ButterKnife;
import butterknife.Bind;

public class CommonDiaryActionBar extends FrameLayout {
    @Bind(R.id.cancel)
    TextView cancelButton;
    @Bind(R.id.confirm)
    TextView confirmButton;
    @Bind(R.id.title)
    TextView titleTxt;

    public CommonDiaryActionBar(Context context) {
        super(context);
        init();
    }

    public CommonDiaryActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttributeSet(attrs, 0);
    }

    public CommonDiaryActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttributeSet(attrs, defStyleAttr);
    }
    private void initAttributeSet(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonDiaryActionBar, defStyle, 0);
        String title_name = typedArray.getString(R.styleable.CommonDiaryActionBar_title_name);
        String cancel_name = typedArray.getString(R.styleable.CommonDiaryActionBar_cancel_name);
        String confirm_name = typedArray.getString(R.styleable.CommonDiaryActionBar_confirm_name);

        if(!TextUtils.isEmpty(title_name)){
            titleTxt.setText(title_name);
        }
        if(!TextUtils.isEmpty(cancel_name)){
            cancelButton.setText(cancel_name);
        }
        if(!TextUtils.isEmpty(confirm_name)){
            confirmButton.setText(confirm_name);
        }
        typedArray.recycle();
    }
    private void init() {
       View view= inflate(getContext(), R.layout.common_diary_action_bar_layout, this);
        ButterKnife.bind(this, view);
    }
    public void setCancelButtonVisibility(int visibility){
        cancelButton.setVisibility(visibility);

    }

    public void setConfirmButtonVisibility(int visibility) {
        confirmButton.setVisibility(visibility);
    }

    public void setTitle(String title) {
        titleTxt.setText(title);
    }
    public void setCancelName(String title){
        cancelButton.setText(title);
    }
    public void setConfirmName(String title){
        confirmButton.setText(title);
    }
}
