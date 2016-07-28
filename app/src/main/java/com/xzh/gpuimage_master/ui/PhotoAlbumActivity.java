package com.xzh.gpuimage_master.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.adapter.AlbumGirdAdapter;
import com.xzh.gpuimage_master.base.BaseActivity;
import com.xzh.gpuimage_master.controller.CameraManager;
import com.xzh.gpuimage_master.model.Album;
import com.xzh.gpuimage_master.model.CloseActivity;
import com.xzh.gpuimage_master.model.PhotoItem;
import com.xzh.gpuimage_master.model.TagImage;
import com.xzh.gpuimage_master.ui.fragment.DiaryDialogFragment;
import com.xzh.gpuimage_master.utils.Constants;
import com.xzh.gpuimage_master.utils.ToastUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PhotoAlbumActivity extends BaseActivity {
    @Bind(R.id.tv_select_album)
    TextView tv_select_album;
    @Bind(R.id.album_gird)
    GridView album_gird;
    @Bind(R.id.iv_cancel)
    ImageView iv_cancel;
    @Bind(R.id.take_pic)
    ImageView take_pic;

    private List<Album> albumList = new ArrayList<>();
    private AlbumGirdAdapter girdAdapter;
    private List<TagImage> tagImageList;
    private Bundle bundle;
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private boolean hasFolderGened = false;
    private ArrayList<Album> mResultFolder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initParam();
        initEvent();
        initData();
    }

    private void initParam() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            tagImageList = (List<TagImage>) bundle.getSerializable(Constants.EXTRA_DATA);
            bundle.remove(Constants.EXTRA_DATA);
        }
    }

    private void initEvent() {
        album_gird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoItem item = (PhotoItem) parent.getItemAtPosition(position);
                if (isAdded(item)) {
                    ToastUtils.shortToast(PhotoAlbumActivity.this, "哈尼,不能添加相同的图片哦");
                    return;
                }
                if (item != null && item.imageUri != null) {
                    File file = new File(item.imageUri);
                    if (file.exists()) {
                        CameraManager.getInst().processPhotoItem(PhotoAlbumActivity.this, item, bundle);
                    }
                }
            }
        });


    }

    private void initData() {
        getSupportLoaderManager().restartLoader(LOADER_ALL, null, new LoaderCallbacksImpl(this));
    }

    public void onEventMainThread(CloseActivity closeActivity) {
        if (closeActivity != null) {
            finish();
        }
    }

    @OnClick(R.id.tv_select_album)
    public void showAlbumPop() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.EXTRA_DATA, (Serializable) albumList);
        DiaryDialogFragment dialogFragment = DiaryDialogFragment.newInstance(bundle);
        dialogFragment.setOnSelect(new DiaryDialogFragment.OnSelectListener() {
            @Override
            public void onSelect(Album album) {
                girdAdapter = new AlbumGirdAdapter(PhotoAlbumActivity.this, album.photos);
                album_gird.setAdapter(girdAdapter);
                tv_select_album.setText(album.title);
            }
        });
        dialogFragment.onDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tv_select_album.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expanded_arrow, 0);
            }
        });
        tv_select_album.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expanded_arrow_up, 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogFragment.show(transaction, "dialogFragment");
    }

    @OnClick(R.id.iv_cancel)
    public void finishActivity() {
        this.finish();
    }

    @OnClick(R.id.take_pic)
    public void takePicture() {
        CameraManager.getInst().openCamera(this, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean isAdded(PhotoItem item) {
        if (item != null && tagImageList != null) {
            for (TagImage tag : tagImageList) {
                if (TextUtils.equals(tag.localPath, item.imageUri)) {
                    return true;
                }
            }
        }
        return false;
    }

    private class LoaderCallbacksImpl implements LoaderManager.LoaderCallbacks<Cursor> {
        Context context;
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        public LoaderCallbacksImpl(Context context) {
            this.context = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(context,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        MediaStore.Images.Media.SIZE + ">?", new String[]{"10000"}, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(context,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data != null) {
                List<PhotoItem> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        if (new File(path).exists()) {
                            PhotoItem image = new PhotoItem(path, dateTime);
                            images.add(image);
                            if (!hasFolderGened) {
                                // 获取文件夹名称
                                File imageFile = new File(path);
                                File folderFile = imageFile.getParentFile();
                                Album folder = new Album();
                                folder.title=folderFile.getName();
                                folder.albumUri=folderFile.getAbsolutePath();
                                if (!mResultFolder.contains(folder)) {
                                    ArrayList<PhotoItem> imageList = new ArrayList<>();
                                    imageList.add(image);
                                    folder.photos=imageList;
                                    mResultFolder.add(folder);
                                } else {
                                    Album album = mResultFolder.get(mResultFolder.indexOf(folder));
                                    album.photos.add(image);
                                }
                            }
                        }
                    } while (data.moveToNext());
                    Album album = null;
                    if (images != null) {
                        album = new Album();
                        album.photos=images;
                        album.title="所有照片";
                        if (girdAdapter == null) {
                            girdAdapter = new AlbumGirdAdapter(context, album.photos);
                            tv_select_album.setText("所有照片");
                            album_gird.setAdapter(girdAdapter);
                        }
                    }
                    if (TextUtils.equals(mResultFolder.get(0).title, "所有照片")) {
                        mResultFolder.remove(0);
                        mResultFolder.add(0, album);
                    } else {
                        mResultFolder.add(0, album);
                    }
                    albumList = mResultFolder;
                    hasFolderGened = true;
                }
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}