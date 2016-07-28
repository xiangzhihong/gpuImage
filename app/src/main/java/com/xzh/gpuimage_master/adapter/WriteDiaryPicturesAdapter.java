package com.xzh.gpuimage_master.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.base.BasicAdapter;
import com.xzh.gpuimage_master.model.TagImage;
import com.xzh.gpuimage_master.utils.UIUtil;
import com.xzh.gpuimage_master.utils.Utils;
import com.xzh.gpuimage_master.utils.imageloader.GPUImageLoader;
import com.xzh.gpuimage_master.view.YmatouDialog;


public class WriteDiaryPicturesAdapter extends BasicAdapter<TagImage> {

    private final Activity context;
    private View itemView = null;
    private View deleteView = null;
    private ImageView picture = null;
    private RelativeLayout.LayoutParams pictureLayoutParams = null;
    private YmatouDialog alertDialog;

    public WriteDiaryPicturesAdapter(final Activity context) {
        int cell = (UIUtil.getScreenWidth(context) - UIUtil.dp2px(50)) / 3;
        pictureLayoutParams = new RelativeLayout.LayoutParams(cell, cell);
        this.context = context;
        alertDialog = new YmatouDialog(context);
        alertDialog.setOnClickListener(new YmatouDialog.OnClickButtonListener() {
            @Override
            public void onClick(View v, YmatouDialog.ClickType type) {
                if (type == YmatouDialog.ClickType.CONFIRM) {
                    remove((TagImage) alertDialog.getData());
                } else {
                }
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initItem(position);
        return convertView;
    }

    private View initItem(int position) {

        if (position == getCount() - 1 && getRealCount() < 6) {
            itemView = View.inflate(context, R.layout.item_layout_add_picture, null);
            View contentView = itemView.findViewById(R.id.content_view);
            contentView.setLayoutParams(pictureLayoutParams);
            if (getRealCount() > 0) {
                TextView tv_hint = (TextView) contentView.findViewById(R.id.tv_hint);
                tv_hint.setHint("好货美照必传哦~");
            }
        } else {
            itemView = View.inflate(context, R.layout.publish_diary_picture_layout, null);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            deleteView = itemView.findViewById(R.id.delete);
            TagImage tagImage = getItem(position);
            deleteView.setOnClickListener(new OnDeleteListener(tagImage));
            GPUImageLoader.loaderRoundImage(tagImage.Pic, picture, 3);
            picture.setLayoutParams(pictureLayoutParams);
        }
        return itemView;
    }

    private class OnDeleteListener implements View.OnClickListener {
        private TagImage tagImage = null;

        public OnDeleteListener(TagImage tagImage) {
            this.tagImage = tagImage;
        }

        @Override
        public void onClick(View v) {
            alertDialog.setTitle("确定删除照片吗？");
            alertDialog.setCancelName("不，点错了");
            alertDialog.setSubmitName("删除照片");
            alertDialog.show(v);
            alertDialog.setData(tagImage);
        }
    }

    public int getRealCount() {
        return super.getCount();
    }

    @Override
    public int getCount() {
        if (getRealCount() < 6) {
            return getRealCount() + 1;
        }
        return getRealCount();
    }
}
