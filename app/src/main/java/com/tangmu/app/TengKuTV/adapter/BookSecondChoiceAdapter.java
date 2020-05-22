package com.tangmu.app.TengKuTV.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class BookSecondChoiceAdapter extends BaseAdapter {

    private List<CategoryBean.SecondBean> secondBeans;
    private Context context;

    public BookSecondChoiceAdapter(List<CategoryBean.SecondBean> secondBeans, Context context) {
        this.secondBeans = secondBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return secondBeans.size();
    }

    @Override
    public CategoryBean.SecondBean getItem(int position) {
        if (position == 0) return null;
        return secondBeans.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book_second, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkedTextView = convertView.findViewById(R.id.check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CategoryBean.SecondBean secondBean = secondBeans.get(position);
        viewHolder.checkedTextView.setText(Util.showText(secondBean.getVt_title(), secondBean.getVt_title_z()));
        return convertView;
    }

    public void setNewData(CategoryBean categoryBean) {
        secondBeans.clear();
        secondBeans.addAll(categoryBean.getSecond());
        notifyDataSetChanged();
    }

    class ViewHolder {
        CheckedTextView checkedTextView;
    }
}
