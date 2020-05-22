package com.tangmu.app.TengKuTV.module.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.LoginContact;
import com.tangmu.app.TengKuTV.presenter.LoginPresenter;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tencent.mm.opensdk.diffdev.DiffDevOAuthFactory;
import com.tencent.mm.opensdk.diffdev.IDiffDevOAuth;
import com.tencent.mm.opensdk.diffdev.OAuthErrCode;
import com.tencent.mm.opensdk.diffdev.OAuthListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements OAuthListener, LoginContact.View {
    @Inject
    LoginPresenter presenter;
    @BindView(R.id.login_code)
    ImageView loginCode;
    private IDiffDevOAuth diffDevOAuth;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        presenter.getToken(timestamp);
    }

    @Override
    public void loginSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void bindThird(String openId, String head_img, int type) {
        Intent intent = new Intent(this, ThirdBindActivity.class);
        intent.putExtra("openId", openId);
        intent.putExtra("type", type);
        intent.putExtra("head_img", head_img);
        startActivity(intent);
    }

    @Override
    public void getSignature(String timestamp, String ticket) {
        try {
            String string = "appid=" + Constant.WXAPP_ID + "&noncestr=" + timestamp + "&sdk_ticket=" + ticket +
                    "&timestamp=" + timestamp;
            LogUtil.e(string);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(string.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String tmp = Integer.toHexString(b & 0xFF);
                if (tmp.length() == 1) {
                    sb.append("0");
                }
                sb.append(tmp);
            }
            LogUtil.e(sb.toString());
            diffDevOAuth = DiffDevOAuthFactory.getDiffDevOAuth();
            diffDevOAuth.auth(Constant.WXAPP_ID, "snsapi_userinfo", timestamp, timestamp, sb.toString(), this);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        diffDevOAuth.removeAllListeners();
        diffDevOAuth.detach();
        super.onDestroy();
    }


    @Override
    protected void initView() {
        presenter.attachView(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onAuthGotQrcode(String s, byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        loginCode.setImageBitmap(bitmap);
    }

    @Override
    public void onQrcodeScanned() {//用户扫描二维码之后，回调该方法
        LogUtil.d("onQrcodeScanned");
    }

    @Override
    public void onAuthFinish(OAuthErrCode oAuthErrCode, String authCode) {//用户点击授权和异常信息，回调该方法
        LogUtil.d("onAuthFinish" + authCode);
        if (oAuthErrCode.getCode() != 0)
            ToastUtil.showText(authCode);
        else {
            presenter.getAccessToken(authCode);
        }
    }

    @Override
    public void showError(String msg) {

    }
}
