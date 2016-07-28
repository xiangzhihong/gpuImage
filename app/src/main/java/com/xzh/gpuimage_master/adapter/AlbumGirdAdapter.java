package com.xzh.gpuimage_master.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.base.BasicAdapter;
import com.xzh.gpuimage_master.model.PhotoItem;
import com.xzh.gpuimage_master.utils.UIUtil;
import com.xzh.gpuimage_master.utils.imageloader.GPUImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlbumGirdAdapter extends BaseAdapter {

    private Context mContext;
    private List<PhotoItem> values;

    public AlbumGirdAdapter(Context context, List<PhotoItem> values) {
        this.mContext = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public PhotoItem getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_layout,null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder,position);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, int position) {
        int screenWidth = UIUtil.getScreenWidth(mContext);
        int width = (screenWidth - UIUtil.dp2px(mContext,10)) / 3 - UIUtil.dp2px(mContext,4);
         AbsListView.LayoutParams params=new AbsListView.LayoutParams(width, width);
         viewHolder.itemImage.setLayoutParams(params);
         imageloader(viewHolder.itemImage,getItem(position).imageUri);
    }


    static class ViewHolder {
        @Bind(R.id.grid_item_image)
        ImageView itemImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

       void resetView(){
        }
    }

    private void imageloader(ImageView imageView, String path) {
        if (!path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("file://")) {
            path = "file://" + path;
        }
        GPUImageLoader.imageloader(path, imageView);
    }

}
