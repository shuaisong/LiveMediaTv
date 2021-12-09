package com.tangmu.app.TengKuTV.module.vip;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.migu.sdk.api.MiguSdk;
import com.shcmcc.tools.GetSysInfo;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.BodyBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.TVProductBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.RechargeVipContact;
import com.tangmu.app.TengKuTV.presenter.RechargeVipPresenter;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.view.CheckableMiGuFrameLayout;
import com.tangmu.app.TengKuTV.view.CheckableMiGuLinearLayout;
import com.tangmu.app.TengKuTV.view.LoadingDialog;
import com.tangmu.app.TengKuTV.view.MiguPay1Dialog;
import com.tangmu.app.TengKuTV.view.MiguPay2Dialog;
import com.tangmu.app.TengKuTV.view.MiguPay3Dialog;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

public class MiGuActivity extends BaseActivity implements CheckableMiGuLinearLayout.OnCheckedChangeListener,
        CheckableMiGuFrameLayout.OnCheckedChangeListener,
        View.OnClickListener, MiguPay1Dialog.PayAgreeListener , RechargeVipContact.View {
    @Inject
    RechargeVipPresenter presenter;

    @BindView(R.id.radio)
    FrameLayout radio;
    @BindView(R.id.vip_j)
    CheckableMiGuLinearLayout checkableMiGuLinearLayout_J;
    @BindView(R.id.vip_m)
    CheckableMiGuLinearLayout checkableMiGuLinearLayout_M;
    @BindView(R.id.vip_y)
    CheckableMiGuLinearLayout checkableMiGuLinearLayout_Y;
    CheckableMiGuLinearLayout mCheckableMiGuLinearLayout;

    @BindView(R.id.m_price)
    TextView mPrice;
    @BindView(R.id.j_price)
    TextView jPrice;
    @BindView(R.id.y_price)
    TextView yPrice;

    @BindView(R.id.radio1)
    FrameLayout radio1;
    @BindView(R.id.pay1)
    CheckableMiGuFrameLayout checkableMiGuFrameLayout1;
    @BindView(R.id.pay2)
    CheckableMiGuFrameLayout checkableMiGuFrameLayout2;
    @BindView(R.id.pay3)
    CheckableMiGuFrameLayout checkableMiGuFrameLayout3;
    CheckableMiGuFrameLayout  mCheckableMiGuFrameLayout;
    private MiguPay1Dialog miguPay1Dialog;
    private MiguPay3Dialog miguPay3Dialog;
    private MiguPay2Dialog miguPay2Dialog;
//    private BodyBean miguBodyBean;
    private LoadingDialog loadingDialog;
    private int payType;
    private Timer payTimer;
    private OrderBean orderBean;
    private long startTime;
    private List<TVProductBean> productBeans;
    private String accountIdentify;
    private AlertDialog miguErrorDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void onDestroy() {
        if (payTimer!=null){
            payTimer.cancel();
            payTimer = null;
        }
        MiguSdk.exitApp(this);
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initData() {
        presenter.attachView(this);
        loadingDialog = new LoadingDialog(this);
        MiguSdk.initializeApp(this);
        loadingDialog.show();
        GetSysInfo getSysInfo = GetSysInfo.getInstance("10086", "", getApplicationContext());
        presenter.miguAuthentications(getSysInfo.getEpgUserId(),getSysInfo.getDeviceId());
    }

    @Override
    protected void initView() {
        checkableMiGuLinearLayout_M.setOnCheckedChangeWidgetListener(this);
        checkableMiGuLinearLayout_J.setOnCheckedChangeWidgetListener(this);
        checkableMiGuLinearLayout_Y.setOnCheckedChangeWidgetListener(this);

        checkableMiGuFrameLayout1.setOnCheckedChangeWidgetListener(this);
        checkableMiGuFrameLayout2.setOnCheckedChangeWidgetListener(this);
        checkableMiGuFrameLayout3.setOnCheckedChangeWidgetListener(this);
        checkableMiGuFrameLayout1.setOnClickListener(this);
        checkableMiGuFrameLayout2.setOnClickListener(this);
        checkableMiGuFrameLayout3.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkableMiGuLinearLayout_M.requestFocus();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_mi_gu;
    }


    @Override
    public void onCheckedChanged(CheckableMiGuFrameLayout checkableMiGuFrameLayout, boolean isChecked) {
        if (isChecked) {
            if (mCheckableMiGuFrameLayout != null)
                mCheckableMiGuFrameLayout.setChecked(false);
            mCheckableMiGuFrameLayout = checkableMiGuFrameLayout;
        } else {
            if (mCheckableMiGuFrameLayout == checkableMiGuFrameLayout) {
                mCheckableMiGuFrameLayout = null;
            }
        }
    }

    @Override
    public void onCheckedChanged(CheckableMiGuLinearLayout checkableMiGuLinearLayout, boolean isChecked) {
        if (isChecked) {
            if (mCheckableMiGuLinearLayout != null)
                mCheckableMiGuLinearLayout.setChecked(false);
            mCheckableMiGuLinearLayout = checkableMiGuLinearLayout;
        } else {
            if (mCheckableMiGuLinearLayout == checkableMiGuLinearLayout) {
                mCheckableMiGuLinearLayout = null;
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pay1) {
            initPay1Dialog();
        } else if (id == R.id.pay2) {
            initPay2Dialog();
        } else if (id == R.id.pay3) {
            initPay3Dialog();
        }
    }

    @Override
    public void onBackPressed() {
        if (miguPay1Dialog!=null&&miguPay1Dialog.isShowing()){
            miguPay1Dialog.dismiss();
        }else if (miguPay2Dialog!=null&&miguPay2Dialog.isShowing()){
            miguPay2Dialog.dismiss();
        }else if (miguPay3Dialog!=null&&miguPay3Dialog.isShowing()){
            miguPay3Dialog.dismiss();
        }else
        super.onBackPressed();
    }

    private void initPay2Dialog() {
        if (miguPay2Dialog ==null){
            miguPay2Dialog = new MiguPay2Dialog(this);
            miguPay2Dialog.setPayAgreeListener(this);
        }
        Integer tag = (Integer) mCheckableMiGuLinearLayout.getTag();
        if (tag==null){
            ToastUtil.showText("数据错误！");
            return;
        }
        payAgree(2);
        TVProductBean productBean = productBeans.get(tag);
        miguPay2Dialog.setPrice(Integer.parseInt(productBean.getPrice())/100+"");
        miguPay2Dialog.show();
    }

    private void initPay3Dialog() {
        if (miguPay3Dialog==null){
            miguPay3Dialog = new MiguPay3Dialog(this);
            miguPay3Dialog.setPayAgreeListener(this);
        }
        Integer tag = (Integer) mCheckableMiGuLinearLayout.getTag();
        if (tag==null){
            ToastUtil.showText("数据错误！");
            return;
        }
        payAgree(1);
        TVProductBean productBean = productBeans.get(tag);
        miguPay3Dialog.setPrice(Integer.parseInt(productBean.getPrice())/100+"");
        miguPay3Dialog.show();
    }

    private void initPay1Dialog() {
        if (miguPay1Dialog ==null){
            miguPay1Dialog = new MiguPay1Dialog(this);
            miguPay1Dialog.setPayAgreeListener(this);
        }

        Integer tag = (Integer) mCheckableMiGuLinearLayout.getTag();
        if (tag==null){
            ToastUtil.showText("数据错误！");
            return;
        }
        TVProductBean productBean = productBeans.get(tag);
        miguPay1Dialog.setPhone(accountIdentify);
        miguPay1Dialog.setPrice(Integer.parseInt(productBean.getPrice())/100+"");
        miguPay1Dialog.show();
    }

    @Override
    public void payAgree(int type) {
        payType = type;
//        presenter.createOrder("2",7,"8802000338","15089020708");
        if ( productBeans!=null&&!productBeans.isEmpty()) {
            Integer tag = (Integer) mCheckableMiGuLinearLayout.getTag();
            if (tag==null){
                ToastUtil.showText("数据错误！");
                return;
            }
            TVProductBean productBean = productBeans.get(tag);
            if (productBean!=null) {
                loadingDialog.show();
                presenter.createOrder(productBean.getPrice()
                        , Integer.parseInt(productBean.getUnit()), productBean.getProductCode(),accountIdentify);
            } else {
                ToastUtil.showText("数据错误！");
            }
        }
    }


    @Override
    public void showOrder(OrderBean orderBean) {
        this.orderBean = orderBean;
        if (loadingDialog.isShowing())loadingDialog.dismiss();
        presenter.pay(payType, orderBean.getOrder_no(), orderBean.getPrice());
    }

    @Override
    public void showPayResult(boolean isSuccess,String msg) {
        if (loadingDialog.isShowing())loadingDialog.dismiss();
        if (payTimer!=null){
            payTimer.cancel();
            payTimer = null;
        }
        if (isSuccess){
            AuthenticationSuccess();
        }else {
            if (miguPay2Dialog!=null&&miguPay2Dialog.isShowing()){
                miguPay2Dialog.dismiss();
            }
            if (miguPay3Dialog!=null&&miguPay3Dialog.isShowing()){
                miguPay3Dialog.dismiss();
            }
            if (miguPay1Dialog!=null&&miguPay1Dialog.isShowing()){
                miguPay1Dialog.dismiss();
            }
            ToastUtil.showText(msg);
        }
//        if (isSuccess) {
//            ToastUtil.showText(getString(R.string.pay_success));
//            // TODO: 2021/10/7
//            MiguLoginBean login = PreferenceManager.getInstance().getLogin();
//            if (login != null) {
//                login.setTu_vip_status(1);
//                PreferenceManager.getInstance().setLogin(login);
//            }
//            setResult(Activity.RESULT_OK);
//            finish();
//        } else {
//            ToastUtil.showText(getString(R.string.pay_fail));
//        }
    }

    @Override
    public void showRechargeBeans(List<TVProductBean> result) {
        if (loadingDialog.isShowing())loadingDialog.dismiss();
        productBeans = result;

        for (TVProductBean productBean : result) {
            switch (productBean.getUnit()){//单位（1.天、2.连续包月、3.单月、4.年、5.季、6.固定时长、7.按次）
                case "2":
                case "3":
                    mPrice.setText(Integer.parseInt(productBean.getPrice())/100+"");
                    checkableMiGuLinearLayout_M.setTag(result.indexOf(productBean));
                    break;
                case "5":
                    jPrice.setText(Integer.parseInt(productBean.getPrice())/100+"");
                    checkableMiGuLinearLayout_J.setTag(result.indexOf(productBean));
                    break;
                case "4":
                    yPrice.setText(Integer.parseInt(productBean.getPrice())/100+"");
                    checkableMiGuLinearLayout_Y.setTag(result.indexOf(productBean));
                    break;
            }
        }
    }

    @Override
    public void showUserInfo(MiguLoginBean userInfoBean) {

    }

    @Override
    public void showPayCode(String result) {
        byte[] decode = Base64.decode(result.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        loadingDialog.dismiss();
        if (payType==1){
            miguPay3Dialog.setImageBitmap(bitmap);
        }else if (payType==2){
            miguPay2Dialog.setImageBitmap(bitmap);
        }
        getPayResult();
    }
    @Override
    public void getPayResult() {
        if (payTimer == null) {
            payTimer = new Timer();
            startTime = System.currentTimeMillis();
            payTimer.schedule(new PayTimeTask(this), 4000, 3000);
        }
    }
    @Override
    public void showError(String msg) {
        if (loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        ToastUtil.showText(msg);
    }

    @Override
    public void showNetError(String msg) {
        if (miguPay1Dialog !=null&& miguPay1Dialog.isShowing()){
            miguPay1Dialog.dismiss();
        }else if (miguPay2Dialog !=null&& miguPay2Dialog.isShowing()){
            miguPay2Dialog.dismiss();
        }else if (miguPay3Dialog !=null&& miguPay3Dialog.isShowing()){
            miguPay3Dialog.dismiss();
        }
        if (loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        ToastUtil.showText(msg);
        if (payTimer!=null){
            payTimer.cancel();
            payTimer = null;
        }
    }

    @Override
    public void showMiguError(String msg) {
        if (loadingDialog.isShowing())loadingDialog.dismiss();
        if (miguErrorDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("支付错误");
            miguErrorDialog = builder.create();
        }
        miguErrorDialog.setMessage(msg);
        miguErrorDialog.show();
    }

    @Override
    public void showPhone(String accountIdentify) {
        this.accountIdentify = accountIdentify;
    }

    @Override
    public void AuthenticationSuccess() {
        ToastUtil.showText("您已是会员");
        MiguLoginBean login = PreferenceManager.getInstance().getLogin();
        login.setTu_vip_status(1);
        PreferenceManager.getInstance().setLogin(login);
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void AuthenticationFail(BodyBean bodyBean){
//        loadingDialog.dismiss();
//        miguBodyBean = bodyBean;
//        List<ProductBean> product = bodyBean.getAuthorize().getProductToOrderList().getProduct();
//        for (ProductBean productBean : product) {
//            switch (productBean.getAttributes().getUnit()){//单位（1.天、2.连续包月、3.单月、4.年、5.季、6.固定时长、7.按次）
//                case "2":
//                case "3":
//                    mPrice.setText(productBean.getAttributes().getPrice());
//                    checkableMiGuLinearLayout_M.setTag(product.indexOf(productBean));
//                    break;
//                case "5":
//                    jPrice.setText(productBean.getAttributes().getPrice());
//                    checkableMiGuLinearLayout_J.setTag(product.indexOf(productBean));
//                    break;
//                case "4":
//                    yPrice.setText(productBean.getAttributes().getPrice());
//                    checkableMiGuLinearLayout_Y.setTag(product.indexOf(productBean));
//                    break;
//            }
//        }
    }

    protected static class PayTimeTask extends TimerTask {

        private final WeakReference<MiGuActivity> activity;
        public PayTimeTask(MiGuActivity activity ) {
            this.activity = new WeakReference<>(activity);
        }

        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
            if (activity.get()==null)return;
            activity.get().presenter.payStatus(activity.get().orderBean.getOrder_no());
            if (System.currentTimeMillis()-60*1000 >= activity.get().startTime){
                cancel();
                activity.get().payTimer.cancel();
                activity.get(). payTimer = null;
                activity.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.get().showPayResult(false,"支付超时");
                    }
                });
            }
        }

    }
}