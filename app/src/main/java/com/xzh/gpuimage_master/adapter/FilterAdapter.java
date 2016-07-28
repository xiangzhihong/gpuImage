package com.xzh.gpuimage_master.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.model.FilterEffect;
import com.xzh.gpuimage_master.utils.DataHandler;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 滤镜适配器
 */
public class FilterAdapter extends BaseAdapter {

    private List<Bitmap> filterList;
    Context mContext;

    private int selectFilter = 0;
    private int selectedPosition;

    public void setSelectFilter(int selectFilter) {
        this.selectFilter = selectFilter;
    }

    public int getSelectFilter() {
        return selectFilter;
    }

    public FilterAdapter(Context context, List<Bitmap> filterList) {
        mContext = context;
        this.filterList = filterList;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bottom_filter, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder,position);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, int position) {
        final FilterEffect effect = DataHandler.filters.get(position);
        viewHolder.smallFilter.setImageBitmap(filterList.get(position));
        viewHolder.filterName.setText(effect.getTitle());
        if (position == selectedPosition) {
            viewHolder.frameLayout.setBackgroundColor(mContext.getResources().getColor(R.color.c9));
        } else {
            viewHolder.frameLayout.setBackgroundColor(mContext.getResources().getColor(R.color.c7));
        }
    }


    class ViewHolder {
        @Bind(R.id.small_filter)
        ImageView smallFilter;
        @Bind(R.id.frame_layout)
        FrameLayout frameLayout;
        @Bind(R.id.filter_name)
        TextView filterName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }

    public void setSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public List<Bitmap> getList() {
        return filterList;
    }
}
