package com.tangmu.app.TengKuTV.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tangmu.app.TengKuTV.R;
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
import me.jessyan.autosize.utils.ScreenUtils;

public class TabAdapter extends CommonNavigatorAdapter {
    protected List<CategoryBean> data;
    protected ItemClickListener itemClickListener;

    public TabAdapter(List<CategoryBean> data, MagicIndicator magicIndicator) {
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
        CategoryBean categoryBean = data.get(index);
        pagerTitleView.setNormalColor(Color.WHITE);
        pagerTitleView.setSelectedColor(Color.WHITE);
        if (categoryBean.getVt_title().equals("VIP")) {
            pagerTitleView.setBackgroundResource(R.mipmap.ic_vip_title);
        } else {
            pagerTitleView.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));
            pagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
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
        indicator.setLineHeight(AutoSizeUtils.dp2px(context, 3));
        indicator.setRoundRadius(AutoSizeUtils.dp2px(context, 1));
        indicator.setYOffset(yOffset);
        indicator.setColors(Color.parseColor("#E33520"));
        return indicator;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClick(int index);
    }

    int yOffset;

    public void setYOff(int off) {
        yOffset = off;
    }
}
