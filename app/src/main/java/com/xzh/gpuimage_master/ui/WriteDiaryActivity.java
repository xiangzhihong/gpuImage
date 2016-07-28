package com.xzh.gpuimage_master.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.adapter.WriteDiaryPicturesAdapter;
import com.xzh.gpuimage_master.base.BaseActivity;
import com.xzh.gpuimage_master.model.ActivityEntity;
import com.xzh.gpuimage_master.model.Diary;
import com.xzh.gpuimage_master.model.OrderDataItem;
import com.xzh.gpuimage_master.model.Tag;
import com.xzh.gpuimage_master.model.TagImage;
import com.xzh.gpuimage_master.utils.ActivityUtil;
import com.xzh.gpuimage_master.utils.Constants;
import com.xzh.gpuimage_master.utils.ToastUtils;
import com.xzh.gpuimage_master.utils.Utils;
import com.xzh.gpuimage_master.view.FixedGridView;
import com.xzh.gpuimage_master.view.YmatouDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class WriteDiaryActivity extends BaseActivity {

    @Bind(R.id.cancel)
    TextView cancelButton;
    @Bind(R.id.pictures)
    FixedGridView pictures;

    private YmatouDialog alertDialog=null;
    private WriteDiaryPicturesAdapter picturesAdapter = null;
    private Diary diary = null;
    private List<Tag> resultList = new ArrayList<>();
    private ActivityEntity activityEntity = null;
    private Activity mContext;
    private OrderDataItem product = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_diary_layout);
        ButterKnife.bind(this);
        mContext = WriteDiaryActivity.this;
        init();
    }

    private void init() {
        initParams();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        picturesAdapter = new WriteDiaryPicturesAdapter(this);
        pictures.setAdapter(picturesAdapter);
        picturesAdapter.setList(diary.TagImages);
    }


    private void initParams() {
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra(Constants.DIARY_OBJ);
        if (diary == null) {
            createDiary();
        } else {
            if (diary.CustomTags != null) {
                resultList.clear();
                resultList.addAll(diary.CustomTags);
            }
            if (!TextUtils.isEmpty(diary.ActivityId) && !"null".equalsIgnoreCase(diary.ActivityId)) {
                Tag tag = new Tag();
                tag.ImageTagType = 4;
                tag.TagVal = diary.ActivityName;
                tag.TagValId = diary.ActivityId;
                resultList.add(tag);
            }
            diary.isUpdateDiry = true;
        }
    }

    private void createDiary() {
        diary = new Diary();
        Intent intent = getIntent();
        product = (OrderDataItem) intent.getSerializableExtra(Constants.DIARY_ORDER_OBJ);
        TagImage tagImage = (TagImage) intent.getSerializableExtra(Constants.DIARY_IMAGE_OBJ);
        if (tagImage != null) {
            insertOrUpdateTagImage(tagImage);
        }
        activityEntity = (ActivityEntity) intent.getSerializableExtra(Constants.DIARY_ACTIVITY_OBJ);
        if (activityEntity != null) {
            diary.ActivityId = "";
            diary.ActivityName = "";
            Tag tag = new Tag();
            tag.ImageTagType = 4;
            tag.TagValId = activityEntity.activityId;
            tag.TagVal = activityEntity.activityName;
            resultList.add(tag);
        }
        diary.Imei = Utils.getDeviceId(this);
        diary.Ip = Utils.getLocalIpAddress(this);
        diary.CkId = "test";
        diary.UserId = "0000";
        diary.UserName = "test";
    }

    private void insertOrUpdateTagImage(TagImage newTagImage) {
        if (diary.TagImages == null) {
            diary.TagImages = new LinkedList<>();
            diary.TagImages.add(newTagImage);
            return;
        }
        if (diary.TagImages.size() > 6) return;
        for (TagImage tagImage : diary.TagImages) {
            if (newTagImage.Pic.equals(tagImage.Pic) || newTagImage.localPath.equals(tagImage.localPath)) {
                tagImage.Pic = newTagImage.Pic;
                tagImage.localPath = newTagImage.localPath;
                tagImage.TagInfo = newTagImage.TagInfo;
                tagImage.filterType = newTagImage.filterType;
                return;
            }
        }
        diary.TagImages.add(newTagImage);
    }

    public void onEventMainThread(TagImage data) {
        insertOrUpdateTagImage(data);
        picturesAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.confirm)
    public void publish() {

    }


    @OnClick(R.id.cancel)
    public void cancel() {
        alertDialog=new YmatouDialog(this);
        alertDialog.setTitle("确定放弃这次笔记修改吗？");
        alertDialog.setCancelName("取消");
        alertDialog.setSubmitName("确定");
        alertDialog.show(cancelButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnItemClick(R.id.pictures)
    public void addOrUpdateImage(AdapterView<?> parent, View view, int position, long id) {
        if (position == picturesAdapter.getCount() - 1 && picturesAdapter.getRealCount() < 6) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.IS_EDIT, true);
            bundle.putSerializable(Constants.EXTRA_DATA, (Serializable) diary.TagImages);
            ActivityUtil.startActivity(this, PhotoAlbumActivity.class, bundle);
        } else {
            TagImage tagImage = picturesAdapter.getItem(position);
            if (tagImage.Pic.startsWith("http://") || tagImage.Pic.startsWith("https://")) {
                tagImage.localPath = tagImage.Pic;
            }
            Intent intent = new Intent(this, ProcessPhotoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.EXTRA_DATA, tagImage);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }

    @Override
    public void finish() {
        EventBus.getDefault().unregister(this);
        super.finish();
    }


}
