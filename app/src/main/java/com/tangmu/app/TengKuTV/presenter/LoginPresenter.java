package com.tangmu.app.TengKuTV.presenter;

import android.util.Base64;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.QQInfoBean;
import com.tangmu.app.TengKuTV.bean.WXinfoBean;
import com.tangmu.app.TengKuTV.bean.WxAccessToken;
import com.tangmu.app.TengKuTV.bean.WxTicketBean;
import com.tangmu.app.TengKuTV.contact.LoginContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

public class LoginPresenter extends RxPresenter<LoginContact.View> implements LoginContact.Presenter {
    @Inject
    public LoginPresenter() {
    }

    @Override
    public void login(String mobile, String password) {
        password = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
        OkGo.<BaseResponse<LoginBean>>post(Constant.IP + Constant.passwordLogin)
                .params("mobile", mobile)
                .params("password", password)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<LoginBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<LoginBean>> response) {
                        if (response.body().getStatus() == 0) {
                            PreferenceManager.getInstance().setLogin(response.body().getResult());
                            view.loginSuccess();
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<LoginBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getAccessToken(String code) {
        OkGo.<Object>post("https://api.weixin.qq.com/sns/oauth2/access_token")
                .params("appid", Constant.WXAPP_ID)
                .params("secret", Constant.WXAPP_SECRET)
                .params("code", code)
                .params("grant_type", "authorization_code")
                .execute(new JsonCallback<Object>() {
                    @Override
                    public void onSuccess(Response<Object> response) {
                        super.onSuccess(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.has("errcode")) {
                                view.showError("授权失败");
                            } else {
                                isAccessValid(jsonObject.getString("openid"), jsonObject.getString("access_token"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            view.showError("授权失败");

                        }

                    }

                    @Override
                    public void onError(Response<Object> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    private void isAccessValid(String openid, String access_token) {
        OkGo.<Object>post("https://api.weixin.qq.com/sns/auth")
                .params("access_token", access_token)
                .params("openid", openid)
                .execute(new JsonCallback<Object>() {
                    @Override
                    public void onSuccess(Response<Object> response) {
                        super.onSuccess(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            int errcode = jsonObject.getInt("errcode");
                            if (errcode == 0) {
                                getUserInfo(openid, access_token);
                            } else {
                                view.showError("授权失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            view.showError("授权失败");
                        }

                    }

                    @Override
                    public void onError(Response<Object> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    private void getUserInfo(String openid, String access_token) {
        OkGo.<WXinfoBean>post("https://api.weixin.qq.com/sns/userinfo")
                .params("access_token", access_token)
                .params("openid", openid)
                .execute(new JsonCallback<WXinfoBean>() {
                    @Override
                    public void onSuccess(Response<WXinfoBean> response) {
                        super.onSuccess(response);
                        isBindMobile(response.body(), null, 1);
                    }

                    @Override
                    public void onError(Response<WXinfoBean> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void isBindMobile(@Nullable WXinfoBean wXinfoBean, @Nullable QQInfoBean qqInfoBean, int type) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.isBindMobile)
                .params("type", type)
                .params("openid", type == 1 ? wXinfoBean.getOpenid() : qqInfoBean.getOpenId())
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Integer>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            if (response.body().getResult() == 0) {
                                if (type == 1)
                                    view.bindThird(wXinfoBean.getOpenid(), wXinfoBean.getHeadimgurl(), type);
                                else {
                                    view.bindThird(qqInfoBean.getOpenId(), qqInfoBean.getHeadImg(), type);
                                }
                            } else {
                                if (type == 1)
                                    thirdLogin(wXinfoBean.getOpenid(), wXinfoBean.getNickname(), wXinfoBean.getHeadimgurl(), type);
                                else
                                    thirdLogin(qqInfoBean.getOpenId(), qqInfoBean.getNickName(), qqInfoBean.getHeadImg(), type);
                            }
                        } else {
                            view.showError("授权失败");
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    private void thirdLogin(String openid, String u_nick_name, String u_img, int type) {
        OkGo.<BaseResponse<LoginBean>>post(Constant.IP + Constant.authCallback)
                .params("u_nick_name", u_nick_name)
                .params("type", type)
                .params("u_img", u_img)
                .params("openid", openid)
                .params("type1", 1)
                .execute(new JsonCallback<BaseResponse<LoginBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<LoginBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            LoginBean result = response.body().getResult();
                            result.setU_img(u_img);
                            PreferenceManager.getInstance().setLogin(result);
                            view.loginSuccess();
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<LoginBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getToken(String timestamp) {
        OkGo.<WxAccessToken>get("https://api.weixin.qq.com/cgi-bin/token")
                .params("grant_type", "client_credential")
                .params("appid", Constant.WXAPP_ID)
                .params("secret", Constant.WXAPP_SECRET)
                .tag(this)
                .execute(new JsonCallback<WxAccessToken>() {
                    @Override
                    public void onSuccess(Response<WxAccessToken> response) {
                        super.onSuccess(response);
                        if (response.body().getErrcode() != 0) {
                            ToastUtil.showText(response.body().getErrmsg());
                        } else {
                            getTicket(timestamp, response.body().getAccess_token());
                        }
                    }

                    @Override
                    public void onError(Response<WxAccessToken> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    private void getTicket(String s, String access_token) {
        OkGo.<WxTicketBean>get("https://api.weixin.qq.com/cgi-bin/ticket/getticket")
                .params("access_token", access_token)
                .params("type", 2)
                .tag(this)
                .execute(new JsonCallback<WxTicketBean>() {
                    @Override
                    public void onSuccess(Response<WxTicketBean> response) {
                        super.onSuccess(response);
                        if (response.body().getErrcode() != 0) {
                            ToastUtil.showText(response.body().getErrmsg());
                        } else {
                            view.getSignature(s, response.body().getTicket());
                        }
                    }

                    @Override
                    public void onError(Response<WxTicketBean> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }
}
