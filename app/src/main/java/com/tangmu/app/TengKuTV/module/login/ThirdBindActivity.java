package com.tangmu.app.TengKuTV.module.login;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.ThirdBindContact;
import com.tangmu.app.TengKuTV.presenter.ThirdBindPresenter;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.TimeCount;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ThirdBindActivity extends BaseActivity implements ThirdBindContact.View {
    @Inject
    ThirdBindPresenter presenter;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.verify_code)
    EditText verifyCode;
    @BindView(R.id.get_verify)
    TextView getVerify;
    @BindView(R.id.bt_register)
    TextView btRegister;
    private TimeCount timeCount;
    private LoadingDialog loadingDialog;
    private String openId;
    private int type;
    private String head_img;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        openId = getIntent().getStringExtra("openId");
        head_img = getIntent().getStringExtra("head_img");
        type = getIntent().getIntExtra("type", 0);
    }


    @Override
    protected void initView() {
        presenter.attachView(this);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_third_bind;
    }

    @OnClick({R.id.get_verify, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verify:
                String phoneStr = phone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr)) {
                    ToastUtil.showText(getString(R.string.account_tip));
                    return;
                }
                presenter.sendVerify(phoneStr);
                if (timeCount == null)
                    timeCount = new TimeCount(60 * 1000, 1000, getVerify);
                timeCount.start();
                break;
            case R.id.bt_register:
                submit();
                break;
        }
    }

    private void submit() {
        String phoneStr = phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtil.showText(getString(R.string.account_tip));
            return;
        }
        String verifyCodeStr = verifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCodeStr)) {
            ToastUtil.showText(getString(R.string.input_verify_tip));
            return;
        }
        loadingDialog.show();
        presenter.thirdLogin(openId, phoneStr, head_img, verifyCodeStr, type);
    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        loadingDialog.dismiss();
        PreferenceManager.getInstance().setLogin(loginBean);
        EventBus.getDefault().post(loginBean);
        finish();
    }

    @Override
    public void finish() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        super.onBackPressed();
    }

    @Override
    public void showVerifyError(String msg) {
        ToastUtil.showText(msg);
        timeCount.cancel();
        getVerify.setText(getString(R.string.get_verify));
        getVerify.setClickable(true);
    }

    @Override
    public void showError(String msg) {
        loadingDialog.dismiss();
        ToastUtil.showText(msg);
    }
}
