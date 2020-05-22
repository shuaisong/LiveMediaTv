package com.tangmu.app.TengKuTV.base;

public interface BaseContact {
    interface BaseView<T> {
       /* void start();

        void compete();*/

        void showError(String msg);
    }

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }
}