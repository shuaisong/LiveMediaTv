package com.tangmu.app.TengKuTV.base;

import com.lzy.okgo.OkGo;

public class RxPresenter<T extends BaseContact.BaseView> implements BaseContact.BasePresenter<T> {
    public T view;

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    public BaseActivity getContext() {
        return (BaseActivity) view;
    }

    @Override
    public void detachView() {
        OkGo.getInstance().cancelTag(this);
    }


}