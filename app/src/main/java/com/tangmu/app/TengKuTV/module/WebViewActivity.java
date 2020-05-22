package com.tangmu.app.TengKuTV.module;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.component.AppComponent;

public class WebViewActivity extends BaseActivity {

    private AgentWeb agentWeb;
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.content), new LinearLayout.LayoutParams(-1, -1))
                .closeIndicator()
//                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected void initView() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (agentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

}
