package com.tangmu.app.TengKuTV.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.tangmu.app.TengKuTV.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class SingleChoiceAdapter extends BaseAdapter {

    private String[] titles;
    private int[] imgIds;
    private Context context;

    public SingleChoiceAdapter(String[] titles, int[] imgIds, Context context) {
        this.titles = titles;
        this.imgIds = imgIds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pay, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkedTextView = convertView.findViewById(R.id.check);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Drawable drawable = context.getResources().getDrawable(imgIds[position]);
        drawable.setBounds(0, 0, AutoSizeUtils.dp2px(context, 23), AutoSizeUtils.dp2px(context, 23));
        Drawable drawable1 = context.getResources().getDrawable(R.drawable.pay_check_select);
        drawable1.setBounds(0, 0, AutoSizeUtils.dp2px(context, 21), AutoSizeUtils.dp2px(context, 21));
        viewHolder.checkedTextView.setCompoundDrawables(drawable, null, drawable1, null);
        viewHolder.checkedTextView.setCompoundDrawablePadding(AutoSizeUtils.dp2px(context, 5));
        viewHolder.checkedTextView.setText(titles[position]);
        return convertView;
    }

    class ViewHolder {
        CheckedTextView checkedTextView;
    }
}
