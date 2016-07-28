package com.xzh.gpuimage_master.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.base.BaseActivity;
import com.xzh.gpuimage_master.listerner.SimpleTextWatcher;
import com.xzh.gpuimage_master.model.EventType;
import com.xzh.gpuimage_master.model.Tag;
import com.xzh.gpuimage_master.model.TagInfo;
import com.xzh.gpuimage_master.utils.Constants;
import com.xzh.gpuimage_master.utils.DataHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class EditTagActivity extends BaseActivity {


    private static final int CATEGORY_CODE = 0x000;
    private static final int COUNTRY_CODE = 0x001;
    private static final int BRANCH_CODE = 0x002;
    private static final int PRICE_CODE = 0x003;

    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.iv_price_clear)
    ImageView ivPriceClear;
    @Bind(R.id.et_country)
    EditText etCountry;
    @Bind(R.id.iv_country_clear)
    ImageView ivCountryClear;
    @Bind(R.id.et_sort)
    EditText etSort;
    @Bind(R.id.iv_sort_clear)
    ImageView ivSortClear;
    @Bind(R.id.et_branch)
    EditText etBranch;
    @Bind(R.id.iv_branch_clear)
    ImageView ivBranchClear;
    @Bind(R.id.tv_del)
    TextView tvDel;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.edit_layout)
    LinearLayout editLayout;
    @Bind(R.id.btn_ok)
    TextView btnOk;
    @Bind(R.id.btn_cancel)
    TextView btnCancel;
    @Bind(R.id.tv_save_enable)
    TextView saveEnable;
    @Bind(R.id.top_layout)
    View topLayout;

    private List<Tag> Tags = new ArrayList<>();
    private HashMap<Integer, Tag> tagMap = new HashMap<>();
    private boolean isEdit;
    private TagInfo tagInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        flagBarTint(false);
        setContentView(R.layout.activity_edit_tag_layout);
        ButterKnife.bind(this);
        isEdit = getIntent().getBooleanExtra(Constants.IS_EDIT, false);
        tagInfo = (TagInfo) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
        initData();
        initView();
        initEvent();

    }

    private void initData() {
        if (!isEdit) return;
        for (Tag tag : tagInfo.Tags) {
            switch (tag.ImageTagType) {
                case 1:
                    tagMap.put(COUNTRY_CODE, tag);
                    break;
                case 2:
                    tagMap.put(CATEGORY_CODE, tag);
                    break;
                case 3:
                    tagMap.put(BRANCH_CODE, tag);
                    break;
            }
        }
    }

    private void initView() {
        if (isEdit) {
            List<Tag> tagList = tagInfo.Tags;
            for (Tag tag : tagList) {
                if (tag.ImageTagType == 0) {
                    etPrice.setText(tag.TagValId);
                } else if (tag.ImageTagType == 1) {
                    etCountry.setText(tag.TagVal);
                } else if (tag.ImageTagType == 2) {
                    etSort.setText(tag.TagVal);
                } else if (tag.ImageTagType == 3) {
                    etBranch.setText(tag.TagVal);
                }
            }
            editLayout.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(etPrice.getText())) {
                ivPriceClear.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(etSort.getText())) {
                ivSortClear.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(etBranch.getText())) {
                ivBranchClear.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(etCountry.getText())) {
                ivCountryClear.setVisibility(View.VISIBLE);
            }
        } else {
            editLayout.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        etPrice.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEnable(isEmpty());
                if (etPrice.getText().toString().length() > 0) {
                    ivPriceClear.setVisibility(View.VISIBLE);
                } else {
                    ivPriceClear.setVisibility(View.INVISIBLE);
                }
            }
        });

        ivPriceClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrice.setText("");
                ivPriceClear.setVisibility(View.INVISIBLE);
                tagMap.remove(PRICE_CODE);
                checkEnable(isEmpty());

            }
        });
        ivBranchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBranch.setText("");
                ivBranchClear.setVisibility(View.INVISIBLE);
                tagMap.remove(BRANCH_CODE);
                checkEnable(isEmpty());

            }
        });
        ivCountryClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCountry.setText("");
                ivCountryClear.setVisibility(View.INVISIBLE);
                tagMap.remove(COUNTRY_CODE);
                checkEnable(isEmpty());
            }
        });
        ivSortClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSort.setText("");
                ivSortClear.setVisibility(View.INVISIBLE);
                tagMap.remove(CATEGORY_CODE);
                checkEnable(isEmpty());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topLayout.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(0, R.anim.out_top_to_bottom);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTagItem();
                Intent intent = new Intent(EditTagActivity.this, ProcessPhotoActivity.class);
                intent.putExtra(Constants.EXTRA_DATA, tagInfo);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.out_top_to_bottom);
            }
        });

        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventType type = new EventType(tagInfo.firstPointX, Constants.DEL_TAG);
                EventBus.getDefault().post(type);
                topLayout.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(0, R.anim.out_top_to_bottom);
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTagItem();
                EventType type = new EventType(tagInfo.firstPointX, Constants.SAVE_TAG);
                type.setItem(tagInfo);
                EventBus.getDefault().post(type);
                topLayout.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(0, R.anim.out_top_to_bottom);
            }
        });
    }


    private void createTagItem() {
        if (isEmpty()) {
            finish();
            return;
        } else {
            if (!TextUtils.isEmpty(etPrice.getText().toString().trim())) {
                Tag priceTag = new Tag();
                priceTag.ImageTagType = 0;
                priceTag.TagValId = etPrice.getText().toString().trim();
                priceTag.TagVal = DataHandler.convertToBigData(etPrice.getText().toString().trim());
                tagMap.put(PRICE_CODE, priceTag);
            }

            for (Map.Entry<Integer, Tag> entry : tagMap.entrySet()) {
                Tags.add(entry.getValue());
            }
            Collections.sort(Tags, new Comparator<Tag>() {
                @Override
                public int compare(Tag lhs, Tag rhs) {
                    return lhs.ImageTagType - rhs.ImageTagType;
                }
            });
            tagInfo.Tags = Tags;
        }
    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(etBranch.getText().toString().trim()) &&
                TextUtils.isEmpty(etCountry.getText().toString().trim()) &&
                TextUtils.isEmpty(etSort.getText().toString().trim()) &&
                TextUtils.isEmpty(etPrice.getText().toString())) {
            if (isEdit) {
                btnOk.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            } else {
                btnOk.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
            return true;
        } else {
            if (isEdit) {
                btnOk.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            } else {
                btnOk.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
            }
            return false;
        }
    }


    private void checkEnable(boolean enable) {
        if (!isEdit) {
            return;
        }
        tvSave.setEnabled(!enable);
        if (enable) {
            tvSave.setVisibility(View.GONE);
            saveEnable.setVisibility(View.VISIBLE);
        } else {
            tvSave.setVisibility(View.VISIBLE);
            saveEnable.setVisibility(View.GONE);
        }
    }
}
