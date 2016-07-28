/*
    The Android Not Open Source Project
    Copyright (c) 2014-9-23 wangzheng <iswangzheng@gmail.com>

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    @author wangzheng  DateTime 2014-9-23
 */

package com.xzh.gpuimage_master.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzh.gpuimage_master.R;
import com.xzh.gpuimage_master.utils.UIUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


public class YmatouDialog implements View.OnClickListener, DialogInterface.OnDismissListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.operation_bar)
    LinearLayout operationBar;

    private View parent = null;
    protected Context mContext = null;
    protected Dialog mDialog = null;
    protected Window mWindow = null;
    //@See 0取消·确定 简约风格； 1 确定 简约风格； 2取消·确定 主题风格； 3 确定 主题风格
    protected LayoutInflater inflaterFactory = null;

    protected OnClickButtonListener onClickListener = null;
    protected View onClickView = null;
    protected Object mData = null;

    public YmatouDialog(Context context) {
        this.mContext = context;
        init();
    }

    public void show() {
        show(mWindow.getDecorView());
    }

    public void show(View v) {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        onClickView = v;
        mDialog.show();
    }

    private void init() {
        inflaterFactory = LayoutInflater.from(mContext);
        initView();
        initDialog();
    }

    private void initDialog() {
        mDialog = new Dialog(mContext, R.style.dialog_style);
        mDialog.setContentView(parent);
        mDialog.setOnDismissListener(this);
        mWindow = mDialog.getWindow();
        mWindow.setWindowAnimations(R.style.centerInCenterOutStyle);
        int width = (int) (UIUtil.getScreenWidth(mContext) * (3.0f / 4.0));
        mWindow.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void initView() {
        parent = inflaterFactory.inflate(R.layout.ymt_dialog_layout, null);
        ButterKnife.bind(this,parent);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        confirm.setOnClickListener(this);

        if (Build.VERSION.SDK_INT <= 11) {
            cancel.setBackgroundResource(R.drawable.comm_button_right_around);
            submit.setBackgroundResource(R.drawable.comm_button_left_around);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            submit(v);
        } else if (v.getId() == R.id.confirm) {
            submit(v);
        } else if (v.getId() == R.id.cancel) {
            cancel(v);
        }
    }

    public void submit(View v) {
        mDialog.dismiss();
        if (onClickListener != null) {
            onClickListener.onClick(onClickView, ClickType.CONFIRM);
        }
    }

    public void cancel(View v) {
        mDialog.dismiss();
        if (onClickListener != null) {
            onClickListener.onClick(onClickView, ClickType.CANCEL);
        }
    }

    public YmatouDialog setContentView(View view) {
        if (view != null) {
            container.addView(view);
        }
        return this;
    }

    public YmatouDialog setCancelable(boolean bool) {
        mDialog.setCancelable(bool);
        return this;
    }

    public YmatouDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public YmatouDialog setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            this.title.setText(title);
        }
        return this;
    }

    public YmatouDialog setSubmitName(CharSequence name) {
        if (!TextUtils.isEmpty(name)) {
            submit.setText(name);
        }
        return this;
    }

    public YmatouDialog setCancelName(CharSequence name) {
        if (!TextUtils.isEmpty(name)) {
            cancel.setText(name);
        }
        return this;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getConfirm() {
        return confirm;
    }

    public TextView getCancel() {
        return cancel;
    }

    public TextView getSubmit() {
        return submit;
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public YmatouDialog setOnClickListener(OnClickButtonListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public interface OnClickButtonListener {
        void onClick(View v, ClickType type);
    }

    public enum ClickType {
        CONFIRM, CANCEL, UNKNOWN;

        @Override
        public String toString() {
            return name();
        }
    }

    public <T extends Object> T getData() {
        return (T) mData;
    }

    public void setData(Object obj) {
        this.mData = obj;
    }

}
