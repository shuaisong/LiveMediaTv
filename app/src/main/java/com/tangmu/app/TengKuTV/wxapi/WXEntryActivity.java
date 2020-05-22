package com.tangmu.app.TengKuTV.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tangmu.app.TengKuTV.Constant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
        initData();
    }

    protected void initData() {
        api = WXAPIFactory.createWXAPI(this, Constant.WXAPP_ID);
        api.handleIntent(getIntent(), this);
    }
/*
    @Override
    protected void initView() {
        money_tv = findViewById(R.id.money);
        pay_status = findViewById(R.id.pay_status);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

/*    @Override
    public int setLayoutId() {
        return R.layout.activity_pay_finish;
    }*/

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
        Log.d(TAG, "onResp, errCode = " + resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == 2) {
                    finish();
                    return;
                }
                String code = ((SendAuth.Resp) resp).code;
                //获取accesstoken
//                getAccessToken(code);
//                Intent intent = new Intent(this, LoginActivity.class);
//                intent.putExtra("code", code);
//                startActivity(intent);
//                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;

        }
    }
}