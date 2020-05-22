package com.tangmu.app.TengKuTV.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;

import com.tangmu.app.TengKuTV.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class CollectTabAdapter extends CommonNavigatorAdapter {
    protected List<String> data;
    private TabAdapter.ItemClickListener itemClickListener;

    public CollectTabAdapter(List<String> data, MagicIndicator magicIndicator) {
        this.data = data;
        this.magicIndicator = magicIndicator;
    }

    protected MagicIndicator magicIndicator;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView pagerTitleView = new SimplePagerTitleView(context);
        pagerTitleView.setNormalColor(context.getResources().getColor(R.color.normal_text_color));
        pagerTitleView.setSelectedColor(Color.parseColor("#0061F0"));
        pagerTitleView.setText(data.get(index));
        pagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
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
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineWidth(AutoSizeUtils.dp2px(context, 30));
        indicator.setLineHeight(AutoSizeUtils.dp2px(context, 2));
        indicator.setRoundRadius(AutoSizeUtils.dp2px(context, 1));
        indicator.setYOffset(yOffset);
        indicator.setColors(Color.parseColor("#0061F0"));
        return indicator;
    }

    public void setItemClickListener(TabAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    int yOffset;

    public void setYOff(int off) {
        yOffset = off;
    }
}
