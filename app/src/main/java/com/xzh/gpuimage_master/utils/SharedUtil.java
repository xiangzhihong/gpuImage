package com.xzh.gpuimage_master.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SharedUtil {
    private static Context context = null;
    private SharedPreferences settings;
    private String name = DEFAULT_SETTING;

    public static final String DEFAULT_SETTING = "default-settings";

    public SharedUtil(Context context, String name) {
        this(context, name, Context.MODE_PRIVATE);
    }

    public static SharedUtil newInstance(Context context) {
        return new SharedUtil(context, DEFAULT_SETTING);
    }

    public static SharedUtil newInstance(Context context, String name) {
        if (TextUtils.isEmpty(name))
            name = DEFAULT_SETTING;
        return new SharedUtil(context, name);
    }

    public SharedUtil(Context context, String name, int mode) {
        SharedUtil.context = context;
        this.name = name;
        settings = context.getSharedPreferences(name, mode);
    }

    public boolean hasExist(String name) {
        return settings.contains(name);
    }

    public String getString(String key) {
        return settings.getString(key, "");
    }

    public int getInt(String key, int def) {
        return settings.getInt(key, def);
    }

    public boolean getBoolean(String key, boolean def) {
        return settings.getBoolean(key, def);
    }

    public float getFloat(String key, float def) {
        return settings.getFloat(key, def);
    }

    public long getLong(String key, int def) {
        return settings.getLong(key, def);
    }

    public Object get(String name) {
        if (hasExist(name)) {
            Map<String, ?> all = settings.getAll();
            return all.get(name);
        }
        return null;
    }

    public List<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        Map<String, ?> all = settings.getAll();
        names.addAll(all.keySet());
        return names;
    }

    public List<String> getList(String key) {
        String jsonStr = getString(key);
        List<String> list = new ArrayList<String>();
        try {
            JSONArray jsonArr = new JSONArray(jsonStr);
            for (int i = jsonArr.length() - 1; i >= 0; i--) {
                String str = jsonArr.getString(i);
                list.add(str);
            }
        } catch (Exception e) {
        }
        return list;
    }

    public <T> void set(String key, T value) {
        if(value == null)return;
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value instanceof CharSequence) {
            editor.putString(key, value.toString());
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Double) {
            editor.putFloat(key, ((Double) value).floatValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else
            editor.putString(name, value.toString());
        editor.commit();
    }

    public <T> void set(String key, List<T> list) {
        JSONArray array = new JSONArray();
        for (T str : list) {
            array.put(str);
        }

        set(key, array.toString());
    }

    public void remove(String key) {
        SharedPreferences sp = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        SharedPreferences sp = context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        Map<String, ?> map = sp.getAll();
        if (map != null) {
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                edit.remove(it.next());
            }
        }
        edit.clear();
        edit.commit();
    }
}
