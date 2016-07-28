package com.xzh.gpuimage_master.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.model.Album;
import com.xzh.gpuimage_master.utils.imageloader.GPUImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlbumsListAdapter extends BaseAdapter {

    private List<Album> albums;
    private Context mContext;

    public AlbumsListAdapter(Context context, List<Album> albums) {
        this.mContext = context;
        this.albums = albums;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (albums != null && !albums.isEmpty()) {
            return albums.size();
        }
        return 0;
    }

    @Override
    public Album getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_layout,null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder,getItem(position));
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, Album item) {
        imageloader(viewHolder.ivSmallPic,item.photos.get(0).imageUri);
        viewHolder.tvAlbumName.setText(item.title);
        viewHolder.tvPicNum.setText(item.photos.size()+"");
    }

    static class ViewHolder {
        @Bind(R.id.iv_small_pic)
        ImageView ivSmallPic;
        @Bind(R.id.tv_album_name)
        TextView tvAlbumName;
        @Bind(R.id.tv_pic_num)
        TextView tvPicNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
        void resetView() {
        }
    }

    private void imageloader(ImageView imageView, String path) {
        if (!path.startsWith("http://") && !path.startsWith("https://") && !path.startsWith("file://")) {
            path = "file://" + path;
        }
        GPUImageLoader.imageloader(path, imageView);
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public List<Album> getAlbums() {
        return albums;
    }

}
