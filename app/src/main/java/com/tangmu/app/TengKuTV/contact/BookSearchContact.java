package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.BookRecmendSeachBean;
import com.tangmu.app.TengKuTV.bean.BookSearchBean;

import java.util.List;

public class BookSearchContact {
    public interface View extends BaseContact.BaseView {
        void showBooks(List<BookSearchBean> videoBeans);

        void showBookOrders(List<BookRecmendSeachBean> serach_book);

    }

    public interface Presenter {

        void getBookRecommend();

        void getBooks(String content);

        void addSearchNum(int b_id);
    }
}
