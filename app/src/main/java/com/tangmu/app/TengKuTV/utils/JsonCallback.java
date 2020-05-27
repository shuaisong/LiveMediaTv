package com.tangmu.app.TengKuTV.utils;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        T data = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (type != null) {
            data = gson.fromJson(jsonReader, type);
        } else if (clazz != null) {
            data = gson.fromJson(jsonReader, clazz);
        } else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            data = gson.fromJson(jsonReader, type);
        }
        return data;
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response.body() instanceof BaseListResponse) {
            if (((BaseListResponse) response.body()).getStatus() == 1001) {
//                response.setException(new Exception("token错误或者失效"));
                response.setException(new Exception("请先注册登录后再使用该功能"));
                onError(response);
                goLogin("请先注册登录后再使用该功能");
                return;
            }
            if (((BaseListResponse) response.body()).getStatus() == 1002) {
                response.setException(new Exception("未检测到信息或者账号禁用"));
                onError(response);
                goLogin("未检测到信息或者账号禁用");
                return;

            }
        }
        if (response.body() instanceof BaseResponse) {
            if (((BaseResponse) response.body()).getStatus() == 1001) {
                //                response.setException(new Exception("token错误或者失效"));
                response.setException(new Exception("请先注册登录后再使用该功能"));
                onError(response);
                goLogin("请先注册登录后再使用该功能");
                return;
            }
            if (((BaseResponse) response.body()).getStatus() == 1002) {
                response.setException(new Exception("未检测到信息或者账号禁用"));
                onError(response);
                goLogin("未检测到信息或者账号禁用");
                return;
            }
        }
        onVerifySuccess(response);
    }

    protected void onVerifySuccess(Response<T> response) {
    }

    private void goLogin(String msg) {
        ToastUtil.showText(msg);
        PreferenceManager.getInstance().exit();
       /* Intent intent = new Intent(CustomApp.getApp(), LoginActivity.class);
        PreferenceManager.getInstance().removeUid();
        PreferenceManager.getInstance().removeToken();
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CustomApp.getApp().startActivity(intent);*/
    }


    protected String handleError(Throwable exception) {
        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            return "网络连接失败,请检查网络!";
        }
        if (exception instanceof SocketTimeoutException) {
            return "网络请求超时!请重试!";
        }
        if (exception instanceof HttpException) {
            return "服务器无响应";
        }
        if (exception instanceof JsonParseException) {
            return "数据解析失败";
        }
        return "未知错误";
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
//        if (!request.getUrl().contains(Constant.login)) {
//            request.params("token", PreferenceManager.getInstance().getLoginInfo().getToken());
//        }
    }
}