package com.tangmu.app.TengKuTV.utils;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.bean.AuthenticationBean;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;

/**
 * Created by lenovo on 2021/10/16.
 * auther:lenovo
 * Date：2021/10/16
 */

public class MiGuJsonCallback extends JsonCallback<String> {
    @Override
    public String convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        String string = body.string();
        return string;
    }

    @Override
    public void onSuccess(Response<String> response) {
        String s = response.body().replaceAll("@", "");
        Gson gson = new Gson();
        AuthenticationBean authenticationBean = gson.fromJson(s, AuthenticationBean.class);
        miguSuccess(authenticationBean);
    }

    protected void miguSuccess(@Nullable AuthenticationBean authenticationBean) {

    }
}
