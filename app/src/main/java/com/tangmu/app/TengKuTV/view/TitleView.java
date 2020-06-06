package com.tangmu.app.TengKuTV.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.module.LogActivity;
import com.tangmu.app.TengKuTV.module.book.BookActivity;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;
import com.tangmu.app.TengKuTV.module.mine.MineActivity;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.search.BookSearchActivity;
import com.tangmu.app.TengKuTV.module.search.VideoSearchActivity;
import com.tangmu.app.TengKuTV.module.vip.VIPActivity;
import com.tangmu.app.TengKuTV.utils.AppLanguageUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;

public class TitleView extends ConstraintLayout implements View.OnClickListener {


    private TextView open_vip;
    private TextView time;

    public TitleView(@NonNull Context context) {
        this(context, null);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_view, this);
        ImageView history = findViewById(R.id.history);
        history.setOnClickListener(this);
        FrameLayout search = findViewById(R.id.search);
        search.setOnClickListener(this);
        FrameLayout mine = findViewById(R.id.mine);
        mine.setOnClickListener(this);
        open_vip = findViewById(R.id.open_vip);
        open_vip.setOnClickListener(this);
        View switchL = findViewById(R.id.switchL);
        TextView tvSwitchL = findViewById(R.id.tv_switchL);
        switchL.setOnClickListener(this);
        ImageView wifi = findViewById(R.id.wifi);
        time = findViewById(R.id.time);
        ImageView imageView = findViewById(R.id.logo);
        imageView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtil.e("hasFocus" + hasFocus);
                if (!hasFocus) imageView.setFocusable(false);
            }
        });
        if (context instanceof BookActivity) {
            imageView.setImageResource(R.mipmap.ic_book_logo);
        }
        findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = getContext();
        switch (v.getId()) {
            case R.id.history:
                if (!(context instanceof PlayHistoryActivity))
                    context.startActivity(new Intent(context, PlayHistoryActivity.class));
                break;
            case R.id.search:
                if (context instanceof BookActivity) {
                    context.startActivity(new Intent(context, BookSearchActivity.class));
                } else
                    context.startActivity(new Intent(context, VideoSearchActivity.class));
                break;
            case R.id.mine:
                if (!(context instanceof MineActivity))
                    context.startActivity(new Intent(context, MineActivity.class));
                break;
            case R.id.open_vip:
                if (PreferenceManager.getInstance().getLogin() != null)
                    context.startActivity(new Intent(context, VIPActivity.class));
                else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                break;
            case R.id.switchL:
                if (context instanceof Activity) {
                    selectLanguage();
                }
                break;
            case R.id.setting:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                try {
                    context.startActivity(intent);
                } catch (Exception ignored) {
                }
                break;
        }
    }


    protected void selectLanguage() {
        //设置语言类型
        PreferenceManager.getInstance().setDefaultLanguage(!PreferenceManager.getInstance().isDefaultLanguage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppLanguageUtils.changeAppLanguage(getContext(),
                    !PreferenceManager.getInstance().isDefaultLanguage() ? "zh" : "bo");
            AppLanguageUtils.changeAppLanguage(CustomApp.getApp(), !PreferenceManager.getInstance().isDefaultLanguage() ? "zh" : "bo");
        } else {
            AppLanguageUtils.changeAppLanguage(getContext(),
                    PreferenceManager.getInstance().isDefaultLanguage() ? "zh" : "bo");
            AppLanguageUtils.changeAppLanguage(CustomApp.getApp(), PreferenceManager.getInstance().isDefaultLanguage() ? "zh" : "bo");
        }
        ((Activity) getContext()).recreate();
    }

    public void setTime(String timeStr) {
        time.setText(timeStr);
    }

    public void updateTV_Vip() {
        LoginBean login = PreferenceManager.getInstance().getLogin();
        if (login == null || login.getU_vip_status() == 0) {
            open_vip.setClickable(true);
            open_vip.setText(getResources().getString(R.string.open_vip));
        } else {
//            open_vip.setClickable(false);
            open_vip.setText(String.format(getContext().getString(R.string.hello_vip), login.getU_nick_name()));
        }
    }
}
