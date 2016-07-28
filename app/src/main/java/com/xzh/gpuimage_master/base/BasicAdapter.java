/*
    The Android Not Open Source Project
    Copyright (c) 2014-11-14 wangzheng <iswangzheng@gmail.com>

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    @author wangzheng  DateTime 2014-11-14
 */

package com.xzh.gpuimage_master.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicAdapter<T> extends BaseAdapter {
    public Context mContext = null;
    protected LayoutInflater inflaterFactory = null;
    protected List<T> mList = new ArrayList<T>();

    public BasicAdapter() {
        super();
    }

    public BasicAdapter(Context context) {
        this.mContext = context;
        inflaterFactory = LayoutInflater.from(mContext);
    }

    public BasicAdapter(List<T> list) {
        if (list != null) {
            mList = list;
        }
    }

    public BasicAdapter(Context context, List<T> list) {
        this(context);
        this.mList = list;
    }

    public void setList(List<T> list) {
        if (list != null) {
            mList = list;
            notifyDataSetChanged();
        }
    }

    public boolean addList(List<T> list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean add(T t) {
        if (t != null) {
            mList.add(t);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean add(int position, T t) {
        if (t != null && getCount() >= position) {
            mList.add(position, t);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void remove(T t) {
        if (mList.remove(t)) {
            notifyDataSetChanged();
        }
    }

    public void remove(List<T> list) {
        if (mList.remove(list)) {
            notifyDataSetChanged();
        }
    }

    public void remove(int index) {
        if (index >= 0 && index < mList.size()) {
            mList.remove(index);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public List<T> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        } else
            return 0;
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    public T getLastItem() {
        if (mList.size() > 0) {
            return mList.get(mList.size() - 1);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public <V extends View> V inflate(int resource, ViewGroup root, boolean attachToRoot) {
        if (inflaterFactory == null) {
            inflaterFactory = LayoutInflater.from(mContext);
        }
        return (V) inflaterFactory.inflate(resource, root, attachToRoot);
    }

    public <V extends View> V inflate(int resource, ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    public <V extends View> V inflate(int resource) {
        return inflate(resource, null, false);
    }
}


