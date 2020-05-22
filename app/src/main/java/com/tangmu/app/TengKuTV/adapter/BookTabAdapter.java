package com.tangmu.app.TengKuTV.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;

import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.utils.Util;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class BookTabAdapter extends CommonNavigatorAdapter {
    protected List<CategoryBean> data;
    protected TabAdapter.ItemClickListener itemClickListener;
    private final int padding;

    public BookTabAdapter(List<CategoryBean> data, MagicIndicator magicIndicator) {
        this.data = data;
        this.magicIndicator = magicIndicator;
        padding = AutoSizeUtils.dp2px(magicIndicator.getContext(), 21.5f);
    }

    protected MagicIndicator magicIndicator;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView pagerTitleView = new SimplePagerTitleView(context);
        pagerTitleView.setNormalColor(Color.WHITE);
        pagerTitleView.setSelectedColor(Color.WHITE);
        CategoryBean categoryBean = data.get(index);
        pagerTitleView.setPadding(padding, 0, padding, 0);
        pagerTitleView.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));
        pagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        pagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                magicIndicator.onPageSelected(index);
                magicIndicator.onPageScrolled(index, 0, 0);
                if (itemClickListener != null) itemClickListener.itemClick(index);
            }
        });
        return pagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        if (getCount() == 0) return null;
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setLineHeight(AutoSizeUtils.dp2px(context, 2));
        indicator.setRoundRadius(AutoSizeUtils.dp2px(context, 1));
        indicator.setColors(Color.parseColor("#E33520"));
        return indicator;
    }

    public void setItemClickListener(TabAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
