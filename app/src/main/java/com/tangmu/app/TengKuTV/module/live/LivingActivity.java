package com.tangmu.app.TengKuTV.module.live;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.GiftBean;
import com.tangmu.app.TengKuTV.bean.LiveBannerBean;
import com.tangmu.app.TengKuTV.bean.LiveDetailBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.LivingContact;
import com.tangmu.app.TengKuTV.module.WebViewActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.vip.VIPActivity;
import com.tangmu.app.TengKuTV.presenter.LivingPresenter;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.TitleView;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.SuperPlayerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class LivingActivity extends BaseActivity implements LivingContact.View {
    @Inject
    LivingPresenter presenter;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.superPlayer)
    SuperPlayerView superPlayer;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.live_time)
    TextView liveTime;
    @BindView(R.id.introl)
    TextView introl;
    @BindView(R.id.line2)
    LinearLayout line2;
    @BindView(R.id.fullscreen)
    ImageView fullscreen;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.line3)
    LinearLayout line3;
    @BindView(R.id.image_recycler)
    RecyclerView imageRecycler;
    @BindView(R.id.video_recycler)
    RecyclerView videoRecycler;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private int roomId;
    private LiveDetailBean liveDetailBean;
    private BaseQuickAdapter<LiveReplayBean, BaseViewHolder> recommendMovieAdapter;
    private VideoAdBean videoAdBean;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        SuperPlayerModel model = new SuperPlayerModel();
        String flvUrl = getIntent().getStringExtra("url");
        String flvUrl1 = getIntent().getStringExtra("url1");
        String flvUrl2 = getIntent().getStringExtra("url2");
        model.url = flvUrl;
        ArrayList<SuperPlayerModel.SuperPlayerURL> superPlayerURLS = new ArrayList<>();
        SuperPlayerModel.SuperPlayerURL superPlayerURL = new SuperPlayerModel.SuperPlayerURL();
        superPlayerURL.url = flvUrl;
        superPlayerURL.qualityName = getString(R.string.source_quality);
        model.playDefaultIndex = 0;
        superPlayerURLS.add(superPlayerURL);

        if (!TextUtils.isEmpty(flvUrl1)) {
            superPlayerURL = new SuperPlayerModel.SuperPlayerURL();
            superPlayerURL.qualityName = getString(R.string._180p);
            superPlayerURL.url = flvUrl1;
            superPlayerURLS.add(superPlayerURL);
        }
        if (!TextUtils.isEmpty(flvUrl2)) {
            superPlayerURL = new SuperPlayerModel.SuperPlayerURL();
            superPlayerURL.qualityName = getString(R.string._270p);
            superPlayerURL.url = flvUrl2;
            superPlayerURLS.add(superPlayerURL);
        }
        model.multiURLs = superPlayerURLS;
        superPlayer.playWithModel(model);
        roomId = getIntent().getIntExtra("id", 0);
        presenter.getLiveDetail(roomId);
        presenter.getTVAd();
        presenter.getRecommend();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        superPlayer.setVideoType(SuperPlayerConst.PLAYTYPE_LIVE);
        superPlayer.isDefaultLanguage(PreferenceManager.getInstance().isDefaultLanguage());
        superPlayer.setRootId(R.id.rootView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recommendMovieAdapter = new BaseQuickAdapter<LiveReplayBean, BaseViewHolder>(R.layout.item_live_rec) {
            @Override
            protected void convert(BaseViewHolder helper, LiveReplayBean item) {
                helper.setText(R.id.title, Util.showText(item.getL_title(), item.getL_title_z()))
                        .setText(R.id.time, item.getLr_add_time());
                GlideUtils.getRequest(LivingActivity.this, Util.convertImgPath(item.getL_img()))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        recommendMovieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveReplayBean item = recommendMovieAdapter.getItem(position);
                if (item == null) return;
                Intent intent = new Intent(LivingActivity.this, HistoryLiveActivity.class);
                intent.putExtra("id", item.getLr_id());
                intent.putExtra("roomId", item.getLr_room_id());
                startActivity(intent);
                finish();
            }
        });
        videoRecycler.setAdapter(recommendMovieAdapter);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_history_live;
    }

    @OnClick({R.id.fullscreen, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fullscreen:
                superPlayer.requestFullMode();
                break;
            case R.id.image:
                if (videoAdBean != null)
                    tvAdClick(videoAdBean);
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        View currentFocus = getCurrentFocus();
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_MENU) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                superPlayer.showMenu();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER||keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN && currentFocus == null) {
                if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE)
                    if (superPlayer.findViewById(R.id.pause_ad_view).getVisibility() == View.VISIBLE) {
                        superPlayer.findViewById(R.id.pause_ad_view).setVisibility(View.GONE);
                    } else
                        superPlayer.onResume();
                else {
                    superPlayer.onPause();
                }
            }
            if (currentFocus != null) {
                switch (currentFocus.getId()) {
                    case R.id.controller_small:
                        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
                            superPlayer.onResume();
                        } else {
                            superPlayer.onPause();
                        }
                        break;
                    case R.id.adView:
                    case R.id.vipTipView:
                    case R.id.buyAntholgyView:
                        if (PreferenceManager.getInstance().getLogin() != null)
                            startActivityForResult(new Intent(LivingActivity.this, VIPActivity.class), 101);
                        else {
                            startActivityForResult(new Intent(LivingActivity.this, LoginActivity.class), 101);
                            EventBus.getDefault().register(LivingActivity.this);
                        }
                        break;
                    case R.id.pause_ad_view:
                        superPlayer.findViewById(R.id.pause_ad_view).setVisibility(View.GONE);
                        break;
                }
            }

        }
        if (currentFocus != null)
            LogUtil.e(currentFocus.toString());
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            superPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
        } else
            super.onBackPressed();
    }

    @Override
    public void collectSuccess(Integer uc_id) {

    }

    @Override
    public void unCollectSuccess() {

    }

    @Override
    public void showLiveDetail(LiveDetailBean result) {
        liveDetailBean = result;
        title.setText(Util.showText(result.getL_title(), result.getL_title_z()));
        liveTime.setVisibility(View.GONE);
        introl.setText(Util.showText(result.getL_des(), result.getL_des_z()));
    }

    @Override
    public void showLiveReply(List<LiveReplayBean> result) {
        recommendMovieAdapter.setNewData(result);
    }

    @Override
    public void showTVAd(List<VideoAdBean> result) {
        if (!result.isEmpty()) {
            image.setVisibility(View.VISIBLE);
            videoAdBean = result.get(0);
            GlideUtils.getRequest(this, Util.convertImgPath(result.get(0).getTa_img()))
                    .transform(new CenterCrop(), new RoundedCorners(AutoSizeUtils.dp2px(this, 15)))
                    .into(image);
            GlideUtils.getRequest(this, Util.convertImgPath(result.get(0).getTa_img()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            superPlayer.setAdImage(resource);
                        }
                    });
        }
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
    }

    private void tvAdClick(VideoAdBean videoAdBean) {
        Intent intent = null;
        switch (videoAdBean.getTa_type()) {
            case 1:
                if (videoAdBean.getVm_type() == 2)
                    intent = new Intent(LivingActivity.this, TVDetailActivity.class);
                else intent = new Intent(LivingActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                intent.putExtra("c_id", videoAdBean.getVt_id_one());
                break;
            case 2:
                intent = new Intent(LivingActivity.this, ShowDubbingVideoActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                break;
            case 3:
                intent = new Intent(LivingActivity.this, WebViewActivity.class);
                intent.putExtra("url", videoAdBean.getTa_url());
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
