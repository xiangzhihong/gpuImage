package com.xzh.gpuimage_master.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.xzh.gpuimage_master.GPUImageApplication;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIUtil {
    private static String TAG = "UI_UTILS";
    private static CharacterStyle fontSpan = new ForegroundColorSpan(Color.BLUE);
    private static boolean exitFlag = false;

    public static int dp2px(float dpValue) {
        final float scale = GPUImageApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static double sp2px(int spValue) {
        final float fontScale = GPUImageApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);	}

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int px2dp(Context context, int pxValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * density);
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    public static double getScreenRate(Context context) {
        return (getScreenHeight(context) + 0.00f) / (getScreenWidth(context) + 0.00f);
    }

    public static View getView(Context context, int layouId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layouId, null);
        return v;
    }


    public static void setTextSize(Context context, TextView tv, int dimen) {
        tv.setTextSize(context.getResources().getDimensionPixelSize(dimen));
        // tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources()
        // .getDimensionPixelSize(dimen));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static final ViewBinder checkboxViewBinder = new ViewBinder() {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            if (view instanceof CheckedTextView) {
                CheckedTextView ctv = (CheckedTextView) view;
                ctv.setText(textRepresentation);
                return true;
            } else {
                return false;
            }
        }
    };

    public static TextWatcher getTextWatcher(final View next, final int length) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s != null && s.toString().length() == length) {
                    next.setFocusable(true);
                    next.setFocusableInTouchMode(true);
                    next.requestFocus();
                }
            }
        };
        return watcher;
    }


    public void resetEditText(ViewGroup view) {
        // 全局遍历 需要为每个控件设定ID
        for (int i = 0; i < view.getChildCount(); i++) {
            View v1 = view.getChildAt(i);
            if (v1 instanceof EditText) {
                EditText e = (EditText) view.findViewById(v1.getId());
                e.setText("");
            }
        }
    }


    /**
     * 获取屏幕宽度的像素
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度像素
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


    public static void hideKeyBoard(Activity context) {
        if (context != null && context.getCurrentFocus() != null) {
            ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(context.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static void showInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 获取sd卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        } else {
            return null;
        }
    }

    /**
     * 是否为手机
     *
     * @param strPhone
     * @return
     */
    public static boolean isPhone(String strPhone) {
        if (TextUtils.isEmpty(strPhone)) {
            return false;
        }
        strPhone = strPhone.replaceAll(" ", "");
        String strPattern = "^((?:13\\d|14[\\d]|15[\\d]|17[\\d]|18[\\d])-?\\d{5}(\\d{3}|\\*{3}))$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strPhone);
        return m.matches();
    }

    /**
     * edittext获得焦点
     * @param context
     */
    public static void getFocus(TextView tv, final Activity context) {
        tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }

    /**
     * 获取bar状态栏的高度，主要为了处理魅族等有bar的系统
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    /**
     * 设置下划线
     * true表明是中下划线，false表明是底部下划线
     */
    public static void setTextViewPaintFlag(TextView view, boolean isCenterFlag) {
        TextPaint mPaint = view.getPaint();
        if (isCenterFlag) {
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.UNDERLINE_TEXT_FLAG);
        }
    }


    /**
     * 显示popupwindow类型的提示
     *
     * @param mContext       上下文
     * @param sourceId       popup布局文件
     * @param anchor         popupwindow显示时参考的锚点view
     * @param messageContent 显示的提示文字
     */
    public static void showPopupWindow(Context mContext, int sourceId, View anchor, int tvRId, String messageContent, int closeRId) {
        View v = LayoutInflater.from(mContext).inflate(sourceId, null);
        TextView tv = (TextView) v.findViewById(tvRId);
        ImageButton ib = (ImageButton) v.findViewById(closeRId);
        tv.setText(messageContent);
        final PopupWindow popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ib.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(anchor);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Activity context) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
