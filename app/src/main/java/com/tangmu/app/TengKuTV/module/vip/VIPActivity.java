package com.tangmu.app.TengKuTV.module.vip;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.RechargeGifBean;
import com.tangmu.app.TengKuTV.bean.UserInfoBean;
import com.tangmu.app.TengKuTV.bean.VipBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.RechargeVipContact;
import com.tangmu.app.TengKuTV.presenter.RechargeVipPresenter;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.CheckableVipLinearLayout;
import com.tangmu.app.TengKuTV.view.LoadingDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class VIPActivity extends BaseActivity implements CheckableVipLinearLayout.OnCheckedChangeListener, RechargeVipContact.View {
    @Inject
    RechargeVipPresenter presenter;
    @BindView(R.id.head)
    ImageView mHead;
    @BindView(R.id.vip_m)
    CheckableVipLinearLayout mVipM;
    @BindView(R.id.vip_j)
    CheckableVipLinearLayout mVipJ;
    @BindView(R.id.vip_y)
    CheckableVipLinearLayout mVipY;
    @BindView(R.id.radio)
    LinearLayout radio;
    private CheckableVipLinearLayout mCheckableVipLinearLayout;
    private LoadingDialog loadingDialog;
    private List<VipBean> vipBeans;
    @BindView(R.id.m_price)
    TextView mPrice;
    @BindView(R.id.j_price)
    TextView jPrice;
    @BindView(R.id.y_price)
    TextView yPrice;
    @BindView(R.id.pay_code)
    ImageView ivPayCode;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        LoginBean login = PreferenceManager.getInstance().getLogin();
        if (login != null)
            setHead(Util.convertImgPath(login.getU_img()), mHead);
        presenter.getVipBeans();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceManager.getInstance().getLogin() != null) {
            presenter.getUserInfo();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        mVipM.setOnCheckedChangeWidgetListener(this);
        mCheckableVipLinearLayout = mVipM;
        mVipJ.setOnCheckedChangeWidgetListener(this);
        mVipY.setOnCheckedChangeWidgetListener(this);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void onCheckedChanged(CheckableVipLinearLayout checkableVipLinearLayout, boolean isChecked) {
        if (isChecked) {
            if (mCheckableVipLinearLayout != null)
                mCheckableVipLinearLayout.setChecked(false);
            mCheckableVipLinearLayout = checkableVipLinearLayout;
            if (vipBeans != null && !vipBeans.isEmpty()) {
                int index = radio.indexOfChild(mCheckableVipLinearLayout);
                ivPayCode.setImageResource(R.color.white);
                presenter.createOrder(vipBeans.get(index).getVm_vip_money(), vipBeans.get(index).getVm_vip_type());
            }
        } else {
            if (mCheckableVipLinearLayout == checkableVipLinearLayout) {
                mCheckableVipLinearLayout = null;
            }
        }
    }

    @Override
    public void showVipBeans(List<VipBean> vipBeans) {
        if (!vipBeans.isEmpty()) {
            this.vipBeans = vipBeans;
            for (VipBean vipBean : vipBeans) {
                if (vipBean.getVm_vip_type() == 1) {
                    mPrice.setText(String.format(getString(R.string.money_unit), vipBean.getVm_vip_money()));
                    presenter.createOrder(vipBeans.get(0).getVm_vip_money(), 1);
                }
                if (vipBean.getVm_vip_type() == 2) {
                    jPrice.setText(String.format(getString(R.string.money_unit), vipBean.getVm_vip_money()));
                }
                if (vipBean.getVm_vip_type() == 3) {
                    yPrice.setText(String.format(getString(R.string.money_unit), vipBean.getVm_vip_money()));
                }
            }

        }
    }

    @Override
    public void showOrder(OrderBean orderBean) {
        loadingDialog.dismiss();
        presenter.weChatPayInfo(orderBean.getOrder_no(), orderBean.getPrice());
    }

    @Override
    public void showPayResult(boolean isSuccess) {
        if (isSuccess) {
            setResult(Activity.RESULT_OK);
            ToastUtil.showText(getString(R.string.pay_success));
            finish();
        } else {
            ToastUtil.showText(getString(R.string.pay_fail));
        }
    }


    @Override
    public void showRechargeBeans(List<RechargeGifBean> result) {

    }

    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {

    }

    @Override
    public void showPayCode(String result) {
        byte[] decode = Base64.decode(result.substring(result.indexOf(",") + 1).getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        ivPayCode.setImageBitmap(bitmap);
    }

    @Override
    public void showError(String msg) {
        loadingDialog.dismiss();
        ToastUtil.showText(msg);
    }
}
