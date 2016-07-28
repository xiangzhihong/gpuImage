package com.xzh.gpuimage_master.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.utils.DataHandler;
import com.xzh.gpuimage_master.utils.UIUtil;
import com.xzh.gpuimage_master.utils.imageloader.GPUImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WaterMarkAdapter extends BaseAdapter {
    private Context context;
    private int size;

    public WaterMarkAdapter(Context context) {
        this.context = context;
        size = UIUtil.getScreenRate(context) > 1.7 ? size = UIUtil.getScreenWidth(context) / 4 + UIUtil.dp2px(10) : UIUtil.getScreenWidth(context) / 4;
    }

    @Override
    public int getCount() {
        return DataHandler.markList.size();
    }

    @Override
    public Object getItem(int position) {
        return DataHandler.markList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_water_layout, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder, position);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.markImg.getLayoutParams();
        params.height = size;
        params.width = size;
        GPUImageLoader.imageloader(DataHandler.markList.get(position).getUri(), viewHolder.markImg);
    }

    static class ViewHolder {
        @Bind(R.id.mark_img)
        ImageView markImg;
        @Bind(R.id.mark_view)
        RelativeLayout markView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }

}
