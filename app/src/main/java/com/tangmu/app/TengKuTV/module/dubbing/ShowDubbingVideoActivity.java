package com.tangmu.app.TengKuTV.module.dubbing;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.DubbingAnthogyBean;
import com.tangmu.app.TengKuTV.bean.DubbingBean;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.DubbingDetailContact;
import com.tangmu.app.TengKuTV.module.WebViewActivity;
import com.tangmu.app.TengKuTV.module.live.HistoryLiveActivity;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.vip.VIPActivity;
import com.tangmu.app.TengKuTV.presenter.DubbingDetailPresenter;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.CustomLoadMoreView;
import com.tangmu.app.TengKuTV.view.TitleView;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.view.TCVodQualityView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CustomAdapt;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class ShowDubbingVideoActivity extends BaseActivity implements CustomAdapt, DubbingDetailContact.View {
    @Inject
    DubbingDetailPresenter presenter;
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
    @BindView(R.id.recyclerview_anthogy)
    RecyclerView recyclerviewAnthogy;
    @BindView(R.id.recyclerview_anthogy1)
    RecyclerView recyclerviewAnthogy1;
    @BindView(R.id.video_recycler)
    RecyclerView videoRecycler;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.collect)
    ImageView collect;
    private Timer timer;
    private DubbingBean dubbingBean;
    private int id;
    private int page = 1;
    private SuperPlayerModel superPlayerModel;
    private int currentPosition = -1;
    private BaseQuickAdapter<DubbingListBean, BaseViewHolder> anthogyAdapter;
    private BaseQuickAdapter<DubbingListBean, BaseViewHolder> recommendAdapter;
    private BaseQuickAdapter<DubbingAnthogyBean, BaseViewHolder> anthogy1Adapter;
    private VideoAdBean videoAdBean;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("id", 0);
        presenter.getDetail(id);
//        presenter.getDubbing(page);
        presenter.getTvAd(PreferenceManager.getInstance().getDubbingId());
        presenter.getRecomend();
    }

    @Override
    protected void onPause() {
        superPlayer.onPause();
        super.onPause();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.e(keyCode + "");
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
                            startActivityForResult(new Intent(ShowDubbingVideoActivity.this, VIPActivity.class), 101);
                        else {
                            startActivityForResult(new Intent(ShowDubbingVideoActivity.this, LoginActivity.class), 101);
                            EventBus.getDefault().register(ShowDubbingVideoActivity.this);
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
    protected void onResume() {
        super.onResume();
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
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
    protected void onDestroy() {
        presenter.detachView();
        superPlayer.resetPlayer();
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        superPlayer.setRootId(R.id.rootView);
        superPlayer.isDefaultLanguage(PreferenceManager.getInstance().isDefaultLanguage());
        superPlayer.setVideoQualityCallback(new TCVodQualityView.VideoQualityCallback() {
            @Override
            public boolean onQualitySelect(TCVideoQuality quality) {
                if (quality.name.equals("HD")) {
                    if (isLogin()) {
                        return false;
                    } else {
                        startActivity(new Intent(ShowDubbingVideoActivity.this, LoginActivity.class));
                        return true;
                    }
                }
                if (quality.name.equals("2K") || quality.name.equals("4K")) {
                    if (isLogin()) {
                        LoginBean login = PreferenceManager.getInstance().getLogin();
                        if (login.getU_vip_status() == 1) {
                            return false;
                        } else {
                            startActivity(new Intent(ShowDubbingVideoActivity.this, VIPActivity.class));
                        }
                    } else {
                        startActivity(new Intent(ShowDubbingVideoActivity.this, LoginActivity.class));
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
        initRecycler();
    }

    private void initRecycler() {
//        initAnthogy();
        recyclerviewAnthogy.setFocusable(false);
//        initAnthogy1();
        recyclerviewAnthogy1.setFocusable(false);
        initRecommend();
    }

    private void initAnthogy1() {
        anthogy1Adapter = new BaseQuickAdapter<DubbingAnthogyBean, BaseViewHolder>(R.layout.item_dubbing_anthogy) {
            @Override
            protected void convert(BaseViewHolder helper, DubbingAnthogyBean item) {
                helper.setText(R.id.item_dubbing_anthogy, (item.getMin() + 1) + "-" + (item.getMax() + 1));
            }
        };
        anthogy1Adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DubbingAnthogyBean item = anthogy1Adapter.getItem(position);
                if (item != null)
                    recyclerviewAnthogy1.scrollToPosition(item.getMin());
            }
        });
        recyclerviewAnthogy1.setAdapter(anthogy1Adapter);
    }

    private void initRecommend() {
        recommendAdapter = new BaseQuickAdapter<DubbingListBean, BaseViewHolder>(R.layout.item_dubbing_rec) {
            @Override
            protected void convert(BaseViewHolder helper, DubbingListBean item) {
                helper.setText(R.id.title, Util.showText(item.getUw_title(), item.getUw_title_z()));
                GlideUtils.getRequest(ShowDubbingVideoActivity.this, Util.convertVideoPath(item.getUw_img()))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DubbingListBean item = recommendAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(ShowDubbingVideoActivity.this, ShowDubbingVideoActivity.class);
                    intent.putExtra("id", item.getUw_id());
                    startActivity(intent);
                    finish();
                }
            }
        });
        videoRecycler.setAdapter(recommendAdapter);
    }

    private void initAnthogy() {
        int currentColor = Color.parseColor("#F93D03");
        int normalColor = getResources().getColor(R.color.normal_text_color);
        anthogyAdapter = new BaseQuickAdapter<DubbingListBean, BaseViewHolder>(R.layout.item_dubbing_list) {
            @Override
            protected void convert(BaseViewHolder helper, DubbingListBean item) {
                if (currentPosition == anthogyAdapter.getData().indexOf(item)) {
                    helper.setTextColor(R.id.title, currentColor);
                } else {
                    helper.setTextColor(R.id.title, normalColor);
                }
                helper.setText(R.id.title, Util.showText(item.getUw_title(), item.getUw_title_z()));
                GlideUtils.getRequest(ShowDubbingVideoActivity.this, Util.convertVideoPath(item.getUw_img()))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        anthogyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (superPlayerModel == null) {
                    superPlayerModel = new SuperPlayerModel();
                }
                int prePosition = currentPosition;
                currentPosition = position;
                anthogyAdapter.notifyItemChanged(prePosition, "");
                anthogyAdapter.notifyItemChanged(currentPosition, "");
                superPlayerModel.url = Util.convertVideoPath(dubbingBean.getUw_url());
                superPlayer.playWithModel(superPlayerModel);
            }
        });
        CustomLoadMoreView customLoadMoreView = new CustomLoadMoreView();
        customLoadMoreView.setEndVisible(false);
        anthogyAdapter.setLoadMoreView(customLoadMoreView);
        anthogyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.getDubbing(page);
            }
        }, recyclerviewAnthogy);
        recyclerviewAnthogy.setAdapter(anthogyAdapter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_show_dubbing_video;
    }


    @Override
    public void showDetail(DubbingBean dubbingBean) {
        this.dubbingBean = dubbingBean;
        if (dubbingBean.getUc_id() != 0) {
            collect.setImageResource(R.drawable.blue_collect_selected);
        } else collect.setImageResource(R.drawable.blue_collect_select);
        introl.setText(Util.showText(dubbingBean.getUw_title(), dubbingBean.getUw_title_z()));
        superPlayerModel = new SuperPlayerModel();
        superPlayerModel.url = Util.convertVideoPath(dubbingBean.getUw_url());
        superPlayer.playWithModel(superPlayerModel);
        liveTime.setText(dubbingBean.getUw_add_time());
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
    }

    @OnClick({R.id.fullscreen, R.id.collect, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image:
                if (videoAdBean != null) {
                    tvAdClick(videoAdBean);
                }
                break;
            case R.id.fullscreen:
                superPlayer.requestFullMode();
                break;
            case R.id.collect:
                if (dubbingBean != null)
                    if (isClickLogin())
                        presenter.collect(id);
                    else presenter.unCollect(dubbingBean.getUc_id());
                break;
        }
    }

    @Override
    public void collectSuccess(int uc_id) {
        dubbingBean.setUc_id(uc_id);
        collect.setImageResource(R.drawable.blue_collect_selected);
    }

    @Override
    public void unCollectSuccess() {
        collect.setImageResource(R.drawable.blue_collect_select);
    }

    @Override
    public void showDubbings(List<DubbingListBean> result) {
        if (page == 1) {
            anthogyAdapter.setNewData(result);
        } else {
            anthogyAdapter.getData().addAll(result);
            if (result.size() < 20) {
                anthogyAdapter.loadMoreEnd();
            } else anthogyAdapter.loadMoreComplete();
        }
        page++;
        int size = anthogyAdapter.getData().size();
        if (size != 0) {
            int a = size / 10;
            List<DubbingAnthogyBean> data = anthogy1Adapter.getData();
            for (int i = 0; i < a + 1; i++) {
                DubbingAnthogyBean dubbingAnthogyBean = new DubbingAnthogyBean();
                dubbingAnthogyBean.setMin(i * 10);
                if (i + 1 < a + 1)
                    dubbingAnthogyBean.setMax((i + 1) * 10);
                data.add(dubbingAnthogyBean);
            }
            int b = size % 10;
            if (b != 0) {
                data.get(data.size() - 1).setMax(a * 10 + b - 1);
            }
            anthogyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showRecmend(List<DubbingListBean> result) {
        recommendAdapter.setNewData(result);
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
                    intent = new Intent(ShowDubbingVideoActivity.this, TVDetailActivity.class);
                else intent = new Intent(ShowDubbingVideoActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                intent.putExtra("c_id", videoAdBean.getVt_id_one());
                break;
            case 2:
                intent = new Intent(ShowDubbingVideoActivity.this, ShowDubbingVideoActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                break;
            case 3:
                intent = new Intent(ShowDubbingVideoActivity.this, WebViewActivity.class);
                intent.putExtra("url", videoAdBean.getTa_url());
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
