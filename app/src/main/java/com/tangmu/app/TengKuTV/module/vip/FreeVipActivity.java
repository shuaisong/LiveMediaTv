package com.tangmu.app.TengKuTV.module.vip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.LaunchAdBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class FreeVipActivity extends BaseActivity {


    @BindView(R.id.vip_receive_success)
    TextView vipReceiveSuccess;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.expire_time)
    TextView expireTime;
    @BindView(R.id.tip1)
    TextView tip1;
    @BindView(R.id.tip2)
    TextView tip2;
    @BindView(R.id.vip_level)
    TextView vipLevel;
    @BindView(R.id.receive_vip)
    TextView receiveVip;
    private boolean isReceived;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        LoginBean login = PreferenceManager.getInstance().getLogin();
        if (login != null) {
            nickName.setText(login.getU_nick_name());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.CHINA);
        expireTime.setText(String.format("腾酷视频会员VIP：%s", simpleDateFormat.format(new Date())));
    }

    @Override
    protected void initView() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_free_vip;
    }

    @OnClick(R.id.receive_vip)
    public void onViewClicked() {
        if (isReceived) finish();
        else
            receiveVip();
    }

    private void receiveVip() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String[] readPhoneState = {Manifest.permission.READ_PHONE_STATE};
            ActivityCompat.requestPermissions(this, readPhoneState, 111);
            return;
        }
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.userVipReceive)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("device_no", telephonyManager.getDeviceId())
                .execute(new JsonCallback<BaseResponse<Integer>>() {

                    @Override
                    public void onSuccess(Response<BaseResponse<Integer>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            vipReceiveSuccess.setVisibility(View.VISIBLE);
                            isReceived = true;
                            tip1.setVisibility(View.INVISIBLE);
                            tip2.setVisibility(View.VISIBLE);
                            receiveVip.setText("确定");
                            LoginBean login = PreferenceManager.getInstance().getLogin();
                            login.setIs_receive(2);
                            login.setU_vip_status(1);
                            login.setU_vip_grade(response.body().getResult());
                            vipLevel.setText(String.valueOf(login.getU_vip_grade()));
                            PreferenceManager.getInstance().setLogin(login);
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public float getSizeInDp() {
        return 700;
    }
}
