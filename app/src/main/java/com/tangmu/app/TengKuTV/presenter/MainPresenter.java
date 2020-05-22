package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.VisitorBean;
import com.tangmu.app.TengKuTV.contact.MainContact;
import com.tangmu.app.TengKuTV.module.home.HomeChildFragment;
import com.tangmu.app.TengKuTV.module.home.HomeDubbingFragment;
import com.tangmu.app.TengKuTV.module.home.HomeVipFragment;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import java.util.List;

import javax.inject.Inject;

public class MainPresenter extends RxPresenter<MainContact.View> implements MainContact.Presenter {
    @Inject
    public MainPresenter() {
    }

    @Override
    public void getVisitor() {
        OkGo.<BaseResponse<VisitorBean>>post(Constant.IP + Constant.getVisitor)
                .execute(new JsonCallback<BaseResponse<VisitorBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<VisitorBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0)
                            PreferenceManager.getInstance().setVisitor(response.body().getResult());
                    }
                });
    }
}
