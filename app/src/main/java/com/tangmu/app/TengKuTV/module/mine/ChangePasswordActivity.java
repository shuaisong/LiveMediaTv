package com.tangmu.app.TengKuTV.module.mine;

import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.DrawEndEditText;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.view.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.old_password)
    DrawEndEditText oldPassword;
    @BindView(R.id.new_password)
    DrawEndEditText newPassword;
    @BindView(R.id.make_sure_password)
    DrawEndEditText makeSurePassword;
    private LoadingDialog loadingDialog;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);
        DrawEndEditText.EndClickListener endClickListener = new DrawEndEditText.EndClickListener() {
            @Override
            public void onClick(DrawEndEditText drawEndEditText) {
                Drawable[] drawables = drawEndEditText.getCompoundDrawablesRelative();
                Drawable endDrawable;
                if (drawEndEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    endDrawable = getResources().getDrawable(R.mipmap.ic_password2);
                    drawEndEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    endDrawable = getResources().getDrawable(R.mipmap.ic_password1);
                    drawEndEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                drawEndEditText.setEndDrawable(endDrawable);
                drawEndEditText.setCompoundDrawablesRelative(drawables[0], drawables[1], endDrawable, drawables[3]);
                drawEndEditText.setSelection(drawEndEditText.length());
            }
        };
        oldPassword.setEndClickListener(endClickListener);
        newPassword.setEndClickListener(endClickListener);
        makeSurePassword.setEndClickListener(endClickListener);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_change_password;
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        String oldPasswordStr = oldPassword.getText().toString().trim();
        if (TextUtils.isEmpty(oldPasswordStr)) {
            ToastUtil.showText(getResources().getString(R.string.input_old_tip));
            return;
        }
        String newPasswordStr = newPassword.getText().toString().trim();
        if (TextUtils.isEmpty(newPasswordStr)) {
            ToastUtil.showText(getResources().getString(R.string.input_new_tip));
            return;
        }
        if (newPasswordStr.length() < 8) {
            ToastUtil.showText(getResources().getString(R.string.input_length_tip));
            return;
        }
        String makeSurePasswordStr = makeSurePassword.getText().toString().trim();
        if (TextUtils.isEmpty(makeSurePasswordStr)) {
            ToastUtil.showText(getResources().getString(R.string.input_make_sure_tip));
            return;
        }
        if (makeSurePasswordStr.length() < 8) {
            ToastUtil.showText(getResources().getString(R.string.input_length_tip));
            return;
        }
        if (!newPasswordStr.equals(makeSurePasswordStr)) {
            ToastUtil.showText(getResources().getString(R.string.make_sure_input_tip));
            return;
        }
        loadingDialog.show();
        submit(oldPasswordStr, newPasswordStr);
    }

    private void submit(String old_password, final String new_password) {
        old_password = Base64.encodeToString(old_password.getBytes(), Base64.DEFAULT);
        String new_password64 = Base64.encodeToString(new_password.getBytes(), Base64.DEFAULT);
        OkGo.<BaseResponse>post(Constant.IP + Constant.savePassword)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("old_password", old_password)
                .params("new_password", new_password64)
                .params("confirm_password", new_password64)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse> response) {
                        loadingDialog.dismiss();
                        if (response.body().getStatus() == 0) {
                            if (PreferenceManager.getInstance().isRemember())
                                PreferenceManager.getInstance()
                                        .setPassword(new_password);
                            ToastUtil.showText(getString(R.string.change_password_success));
                            finish();
                        } else ToastUtil.showText(response.body().getMsg());
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);
                        loadingDialog.dismiss();
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }
}
