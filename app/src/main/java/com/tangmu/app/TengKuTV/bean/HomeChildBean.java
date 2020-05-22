package com.tangmu.app.TengKuTV.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class HomeChildBean implements Serializable, MultiItemEntity {
    public static final int TITLE = 0;
    public static final int MOVIE = 1;
    private HomeChildRecommendBean.VideoBean movieBean;
    private TitleBean titleBean;

    public HomeChildRecommendBean.VideoBean getMovieBean() {
        return movieBean;
    }

    public void setMovieBean(HomeChildRecommendBean.VideoBean movieBean) {
        this.movieBean = movieBean;
    }

    public TitleBean getTitleBean() {
        return titleBean;
    }

    public void setTitleBean(TitleBean titleBean) {
        this.titleBean = titleBean;
    }

    @Override
    public int getItemType() {
        if (titleBean != null) return TITLE;
        if (movieBean != null) return MOVIE;
        return 0;
    }
}
