package com.tangmu.app.TengKuTV.module.mine;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.bean.VersionBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.vip.MiGuActivity;
import com.tangmu.app.TengKuTV.utils.InstallUtil;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.LoadingDialog;

import java.io.File;
import java.math.BigDecimal;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.quality1)
    RadioButton quality1;
    @BindView(R.id.quality2)
    RadioButton quality2;
    @BindView(R.id.quality3)
    RadioButton quality3;
    @BindView(R.id.quality4)
    RadioButton quality4;
    @BindView(R.id.quality5)
    RadioButton quality5;
    @BindView(R.id.radio_quality)
    RadioGroup radioQuality;
    @BindView(R.id.jump)
    RadioButton jump;
    @BindView(R.id.no_jump)
    RadioButton noJump;
    @BindView(R.id.radio_jump)
    RadioGroup radioJump;
    @BindView(R.id.clear_cache)
    TextView clearCache;
    @BindView(R.id.current_version)
    TextView currentVersion;
    @BindView(R.id.tv_is_new)
    TextView tvIsNew;
    private LoadingDialog loadingDialog;
    private VersionBean versionBean;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        currentVersion.setClickable(false);
        boolean isJump = PreferenceManager.getInstance().getIsJump();
        if (isJump) {
            radioJump.check(R.id.jump);
        } else {
            radioJump.check(R.id.no_jump);
        }
        int defaultQuality = PreferenceManager.getInstance().getDefaultQuality();
        if (5 - defaultQuality < radioQuality.getChildCount())
            radioQuality.check(radioQuality.getChildAt(5 - defaultQuality).getId());
        getCache();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion.setText(String.format(getString(R.string.current_version), packageInfo.versionCode));
            getNewVersion(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getNewVersion(int versionCode) {
        OkGo.<BaseResponse<VersionBean>>post(Constant.IP + Constant.detectionNewVersion)
                .params("v_type", 3)
                .params("code", versionCode).tag(this)
                .execute(new JsonCallback<BaseResponse<VersionBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<VersionBean>> response) {
                        if (response.body().getStatus() == 0) {
                            versionBean = response.body().getResult();
                            currentVersion.setText(String.format(getString(R.string.new_version_name), versionBean.getV_code()));
                            tvIsNew.setVisibility(View.INVISIBLE);
                            currentVersion.setClickable(true);
                        } else {
                            tvIsNew.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<VersionBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });


    }

    @Override
    protected void initView() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            currentVersion.setText(String.format(getString(R.string.current_version), packageInfo.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_setting;
    }


    @OnClick({R.id.clear_cache, R.id.current_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.quality1:
                MiguLoginBean login = PreferenceManager.getInstance().getLogin();
                if (login.getTu_vip_status()!=1)
                    startActivity(new Intent(this, MiGuActivity.class));
                break;
//            case R.id.quality2:
//                if (!isClickLogin()) {
//                    radioQuality.check(-1);
//                    startActivity(new Intent(this, LoginActivity.class));
//                }
//                break;
            case R.id.clear_cache:
                Util.deleteDir(getCacheDir(), false);
                if (getExternalCacheDir() != null)
                    Util.deleteDir(getExternalCacheDir(), false);
                getCache();
                break;
            case R.id.current_version:
                if (versionBean != null) {
                    downLoadApk(Constant.Pic_IP + versionBean.getV_url());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getCache() {
        long cache = 0;
        cache += Util.computeFolderSize(getExternalCacheDir());
        cache += Util.computeFolderSize(getCacheDir());
        setCache(cache);
    }

    private void setCache(long cache) {
        BigDecimal aBigDecimal = new BigDecimal(cache);
        BigDecimal bBigDecimal = new BigDecimal(1024 * 1024);
        float value = aBigDecimal.divide(bBigDecimal, 2, BigDecimal.ROUND_HALF_UP).setScale(2).floatValue();
        clearCache.setText(String.format("%s%sMB", getString(R.string.cache), value));
    }

    @Override
    public void finish() {
        PreferenceManager.getInstance().setIsJump(radioJump.getCheckedRadioButtonId() == R.id.jump);
        if (radioQuality.getCheckedRadioButtonId() != -1)
            PreferenceManager.getInstance().setDefaultQuality(5 - radioQuality.indexOfChild(
                    radioQuality.findViewById(radioQuality.getCheckedRadioButtonId())));
        super.finish();
    }

    private void downLoadApk(String url) {
        OkGo.<File>get(url).params("If-Modified-Since", System.currentTimeMillis())
                .execute(new FileCallback(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                        , "tengku.apk") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        InstallUtil.installApk(response.body().getAbsolutePath());
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        File file = new File(Environment.
                                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tengku.apk");
                        if (file.exists()) file.delete();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        ToastUtil.showText(Util.handleError(response.getException()));
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        loadingDialog.setProgress((int) (progress.fraction * 100));
                    }
                });
    }

}
