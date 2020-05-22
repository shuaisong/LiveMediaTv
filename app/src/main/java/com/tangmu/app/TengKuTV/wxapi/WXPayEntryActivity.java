package com.tangmu.app.TengKuTV.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private TextView money_tv, pay_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pay_finish);
        initData();
    }

    protected void initData() {
        api = WXAPIFactory.createWXAPI(this, Constant.WXAPP_ID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                ToastUtil.showText(getString(R.string.pay_success));
            } else {
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        ToastUtil.showText(getString(R.string.pay_cancel));
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        ToastUtil.showText("支付被拒绝");
                        break;
                    case BaseResp.ErrCode.ERR_SENT_FAILED:
                        ToastUtil.showText(getString(R.string.pay_fail));
                        break;
                    case BaseResp.ErrCode.ERR_UNSUPPORT:
                        LogUtil.d("配置错误");
                        ToastUtil.showText(getString(R.string.pay_fail));
                        break;
                }
            }
            finish();
        }
    }
}