package com.tangmu.app.TengKuTV.module.live;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.LiveHistoryBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.LiveHistoryContact;
import com.tangmu.app.TengKuTV.module.WebViewActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.vip.VIPActivity;
import com.tangmu.app.TengKuTV.presenter.LiveHistoryPresenter;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.TitleView;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.bean.VideoBean;
import com.tencent.liteav.demo.play.bean.VideoSortBean;
import com.tencent.liteav.demo.play.utils.AnthologyItemDecoration;
import com.tencent.liteav.demo.play.view.TCVodQualityView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class HistoryLiveActivity extends BaseActivity implements LiveHistoryContact.View {

    @Inject
    LiveHistoryPresenter presenter;
    @BindView(R.id.superPlayer)
    SuperPlayerView superPlayer;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.live_time)
    TextView liveTime;
    @BindView(R.id.introl)
    TextView introl;
    @BindView(R.id.line2)
    LinearLayout line2;
    @BindView(R.id.line3)
    LinearLayout line3;
    @BindView(R.id.image_recycler)
    RecyclerView imageRecycler;
    @BindView(R.id.video_recycler)
    RecyclerView videoRecycler;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.image)
    ImageView image;
    private int id;
    private LiveHistoryBean liveHistoryBean;
    private BaseQuickAdapter<LiveReplayBean, BaseViewHolder> recommendMovieAdapter;
    private Timer timer;
    private VideoAdBean videoAdBean;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        presenter.getDetail(id);
        presenter.getRecommend();
        presenter.getTVAd();
    }


    @Override
    protected void onPause() {
        super.onPause();
        superPlayer.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.e(keyCode + "");
        View currentFocus = getCurrentFocus();
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_MENU) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                superPlayer.showMenu();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
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
                        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE || superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_END) {
                            superPlayer.onResume();
                        } else {
                            superPlayer.onPause();
                        }
                        break;
                    case R.id.adView:
                    case R.id.vipTipView:
                    case R.id.buyAntholgyView:
                        if (PreferenceManager.getInstance().getLogin() != null)
                            startActivityForResult(new Intent(HistoryLiveActivity.this, VIPActivity.class), 101);
                        else {
                            startActivityForResult(new Intent(HistoryLiveActivity.this, LoginActivity.class), 101);
                            EventBus.getDefault().register(HistoryLiveActivity.this);
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
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        if (superPlayer != null)
            superPlayer.resetPlayer();
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (superPlayer != null && superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
            superPlayer.onResume();
        }
        titleView.updateTV_Vip();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long currentTimeMillis = System.currentTimeMillis();
                            titleView.setTime(Util.convertSystemTime(currentTimeMillis));
                        }
                    });
                }
            }, 0, 1000);
        }
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        id = getIntent().getIntExtra("id", 0);
//        initAnthologyList();
        initRecommendList();
        superPlayer.isDefaultLanguage(PreferenceManager.getInstance().isDefaultLanguage());
        superPlayer.setVideoType(SuperPlayerConst.PLAYTYPE_LIVE_SHIFT);
        superPlayer.setRootId(R.id.rootView);
        superPlayer.setVideoQualityCallback(new TCVodQualityView.VideoQualityCallback() {
            @Override
            public boolean onQualitySelect(TCVideoQuality quality) {
                if (quality.name.equals("HD")) {
                    if (isLogin()) {
                        return false;
                    } else {
                        startActivity(new Intent(HistoryLiveActivity.this, LoginActivity.class));
                        return true;
                    }
                }
                if (quality.name.equals("2K") || quality.name.equals("4K")) {
                    if (isLogin()) {
                        LoginBean login = PreferenceManager.getInstance().getLogin();
                        if (login.getU_vip_status() == 1) {
                            return false;
                        } else {
                            startActivity(new Intent(HistoryLiveActivity.this, VIPActivity.class));
                        }
                    } else {
                        startActivity(new Intent(HistoryLiveActivity.this, LoginActivity.class));
                        return true;
                    }
                }
                return false;
            }
        });
        int defaultQuality = PreferenceManager.getInstance().getDefaultQuality();
        LoginBean login = PreferenceManager.getInstance().getLogin();
        PreferenceManager.getInstance().getLogin();
        if (defaultQuality > 4) {
            if (login != null && login.getU_vip_status() == 1) {
                superPlayer.setDefaultQualitySet(defaultQuality);
            }
        } else if (defaultQuality > 3) {
            if (login != null) {
                superPlayer.setDefaultQualitySet(defaultQuality);
            }
        } else {
            superPlayer.setDefaultQualitySet(defaultQuality);
        }
    }


    private void initRecommendList() {
        recommendMovieAdapter = new BaseQuickAdapter<LiveReplayBean, BaseViewHolder>(R.layout.item_live_rec) {
            @Override
            protected void convert(BaseViewHolder helper, LiveReplayBean item) {
                helper.setText(R.id.title, Util.showText(item.getL_title(), item.getL_title_z()))
                        .setText(R.id.time, item.getLr_add_time());
                GlideUtils.getRequest(HistoryLiveActivity.this, Util.convertImgPath(item.getL_img()))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        recommendMovieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveReplayBean item = recommendMovieAdapter.getItem(position);
                if (item == null) return;
                Intent intent = new Intent(HistoryLiveActivity.this, HistoryLiveActivity.class);
                intent.putExtra("id", item.getLr_id());
                intent.putExtra("roomId", item.getLr_room_id());
                startActivity(intent);
                finish();
            }
        });
        videoRecycler.setAdapter(recommendMovieAdapter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_history_live;
    }


    @Override
    public void showDetail(LiveHistoryBean liveHistoryBean) {
        this.liveHistoryBean = liveHistoryBean;
        SuperPlayerModel model = new SuperPlayerModel();
        model.appId = Constant.PLAYID;// 配置 AppId
        model.videoId = new SuperPlayerVideoId();
        model.videoId.fileId = liveHistoryBean.getLr_fileid();
        title.setText(Util.showText(liveHistoryBean.getL_title(), liveHistoryBean.getL_title_z()));
        liveTime.setText(liveHistoryBean.getLr_add_time());
        introl.setText(Util.showText(liveHistoryBean.getDes(), liveHistoryBean.getDes_z()));
        superPlayer.playWithModel(model);
    }

    @Override
    public void showLiveReply(List<LiveReplayBean> result) {
        recommendMovieAdapter.setNewData(result);
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
    }


    @Override
    public void onBackPressed() {
        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            superPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
        } else
            super.onBackPressed();
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

    private void tvAdClick(VideoAdBean videoAdBean) {
        Intent intent = null;
        switch (videoAdBean.getTa_type()) {
            case 1:
                if (videoAdBean.getVm_type() == 2)
                    intent = new Intent(HistoryLiveActivity.this, TVDetailActivity.class);
                else intent = new Intent(HistoryLiveActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                intent.putExtra("c_id", videoAdBean.getVt_id_one());
                break;
            case 2:
                intent = new Intent(HistoryLiveActivity.this, ShowDubbingVideoActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                break;
            case 3:
                intent = new Intent(HistoryLiveActivity.this, WebViewActivity.class);
                intent.putExtra("url", videoAdBean.getTa_url());
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
