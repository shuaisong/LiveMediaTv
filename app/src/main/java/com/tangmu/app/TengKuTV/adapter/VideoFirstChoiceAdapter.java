package com.tangmu.app.TengKuTV.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class VideoFirstChoiceAdapter extends BaseAdapter {

    private List<CategoryBean> categoryBeans;
    private Context context;

    public VideoFirstChoiceAdapter(List<CategoryBean> categoryBeans, Context context) {
        this.categoryBeans = categoryBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryBeans.size();
    }

    @Override
    public CategoryBean getItem(int position) {
        return categoryBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_first, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkedTextView = convertView.findViewById(R.id.check_first);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CategoryBean categoryBean = categoryBeans.get(position);
        viewHolder.checkedTextView.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));
        return convertView;
    }

    public void setNewData(List<CategoryBean> categoryBean) {
        categoryBeans.clear();
        categoryBeans.addAll(categoryBean);
        notifyDataSetChanged();
    }

    class ViewHolder {
        CheckedTextView checkedTextView;
    }
}
